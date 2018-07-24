package pl.kamilbeben.kittenserver.abstracted;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import pl.kamilbeben.kittenserver.events.PlayerJoinEvent;
import pl.kamilbeben.kittenserver.required.PlayerHolder;
import pl.kamilbeben.kittenserver.events.PlayerDisconnectEvent;
import pl.kamilbeben.kittenserver.packets.GenericPacket;
import pl.kamilbeben.kittenserver.properties.RoomProperties;
import pl.kamilbeben.kittenserver.required.WebSocketWrapper;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

/**
 * @param <T> your implementation of {@link AbstractPlayer}
 * @param <R> your's implementation of {@link AbstractRoom}
 */
@Scope(SCOPE_SINGLETON)
@EnableConfigurationProperties(RoomProperties.class)
public abstract class AbstractLobbyService<T extends AbstractPlayer<R>, R extends AbstractRoom<T>> {

  @Autowired protected WebSocketWrapper webSocket;
  @Autowired protected PlayerHolder playerHolder;
  @Autowired protected RoomProperties roomProperties;

  // rooms
  protected final Set<R> rooms = new HashSet<>();

  protected final Set<T> playersInQueue = new HashSet<>();

  protected abstract T constructPlayer(Principal principal, Consumer<GenericPacket> sendPacket);
  protected abstract R constructRoom(Set<T> players, RoomProperties config);

  @EventListener(PlayerJoinEvent.class)
  public void registerPlayer(PlayerJoinEvent event) {
    T player = constructPlayer(event.getSource(), webSocket::send);
    playerHolder.addPlayer(player);
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
    synchronized (playersInQueue) {
      playersInQueue.remove(player);
    }
  }

  public void joinPublicQueue(Principal principal) {
    T player = getPlayer(principal);
    if (player.getRoom() != null) return;

    synchronized (playersInQueue) {
      playersInQueue.add(player);
      refreshQueue();
    }
  }

  /**
   * If you need to implement custom queueing or room creation policy, then overriding this method is a good place to start.
   * Kepp in mind that it's execution is synchronized on {@link #playersInQueue}!
   */
  protected void refreshQueue() {

    int playersCount = playersInQueue.size();

    // first player joined queue, start counting
    if (playersCount == 1) {
      // TODO think of something clever
      return;
    }

    // sequent player joined queue, but minimum players limit was not yet reached
    if (playersCount == roomProperties.getMinPlayers()) {
      startNewPublicRoom();
    };

    // sequent player joined queue, maximum players limit was reached, starting new room
    if (playersCount >= roomProperties.getMaxPlayers()) {
      startNewPublicRoom();
    }
  }

  protected T getPlayer(Principal principal) {
    AbstractPlayer player = playerHolder.getPlayer(principal);
    return player != null
      ? (T) player
      : null;
  }

  private void startNewPublicRoom() {
    HashSet<T> clonedPlayers = new HashSet<>(playersInQueue);
    playersInQueue.clear();

    R newRoom = createNewRoom(clonedPlayers, false);

    newRoom.start();
  }

  private R createNewRoom(Set<T> players, boolean isPrivate) {
    R room = constructRoom(players, roomProperties);

    rooms.add(room);
    return room;
  }
}
