package kittenserver.required;

import kittenserver.events.PlayerDisconnectEvent;
import kittenserver.events.PlayerJoinEvent;
import kittenserver.properties.RoomProperties;
import kittenserver.beans.WebSocketWrapper;
import kittenserver.example.ExampleLobbyService;
import kittenserver.packets.GenericPacket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.event.EventListener;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @param <T> your implementation of {@link AbstractPlayer}
 * @param <R> your's implementation of {@link AbstractRoom}
 *
 * @see ExampleLobbyService
 */
@EnableConfigurationProperties({
  RoomProperties.class
})
public abstract class AbstractLobbyService<T extends AbstractPlayer<R>, R extends AbstractRoom<T>> {

  @Autowired protected WebSocketWrapper webSocket;
  @Autowired protected AbstractPlayerHolder<T> playerHolder;
  @Autowired protected RoomProperties roomProperties;

  // rooms
  protected final Set<R> rooms = new HashSet<>();

  protected final Set<T> playersInLobby = new HashSet<>();

  protected abstract T constructPlayer(Principal principal, Consumer<GenericPacket> sendPacket);
  protected abstract R constructRoom(Set<T> players, RoomProperties config);

  @EventListener(PlayerJoinEvent.class)
  public void registerPlayer(PlayerJoinEvent event) {
    T player = constructPlayer(event.getSource(), webSocket::send);
    playerHolder.addPlayer(player);

    synchronized (playersInLobby) {
      playersInLobby.add(player);
      afterPlayerRegistered();
    }
  }

  @EventListener(PlayerDisconnectEvent.class)
  public void removePlayer(PlayerDisconnectEvent event) {
    T player = (T) event.getSource();

    // remove player from room if already in one
    AbstractRoom room = player.getRoom();
    if (room != null) {
      room.remove(player);
      return;
    }

    // if player is not in room, remove him from lobby
    synchronized (playersInLobby) {
      playersInLobby.remove(player);
    }
  }

  /**
   * If you need to implement custom queueing or room creation policy, then overriding this method is a good place to start.
   * Kepp in mind that it's execution is synchronized on {@link #playersInLobby}!
   */
  protected void afterPlayerRegistered() {

    int playersCount = playersInLobby.size();

    // first player joined queue, start counting
    if (playersCount == 1) {
      // TODO think of something clever
      return;
    }

    // sequent player joined queue, but minimum players limit was not yet reached
    if (playersCount == roomProperties.getMinPlayers()) {
      startNewRoom();
    };

    // sequent player joined queue, maximum players limit was reached, starting new room
    if (playersCount >= roomProperties.getMaxPlayers()) {
      startNewRoom();
    }
  }

  private void startNewRoom() {
    R newRoom = createNewRoom(new HashSet<>(playersInLobby));
    newRoom.start();
    playersInLobby.clear();
  }

  private R createNewRoom(Set<T> players) {
    R room = constructRoom(players, roomProperties);
    rooms.add(room);
    return room;
  }
}
