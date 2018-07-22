package kittenserver.required;

import kittenserver.config.RoomConfig;
import kittenserver.events.WebSocketWrapper;
import kittenserver.example.ExampleLobbyService;
import kittenserver.packets.GenericPacket;
import lombok.Getter;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

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
public abstract class AbstractLobbyService<T extends AbstractPlayer<R>, R extends AbstractRoom<T>> {

  @Autowired protected WebSocketWrapper webSocket;
  @Autowired protected AbstractPlayerHolder<T> playerHolder;

  // room roomConfig
  @Value("${game.room.max.players}")
  private int roomMaxPlayers;

  @Value("${game.room.update.packet.interval}")
  private long roomUpdatePacketInterval;

  @Value("${game.room.update.destination}")
  private String roomUpdateDestination;

  @Getter(lazy = true)
  private final RoomConfig roomConfig = prepareRoomConfig();

  // rooms
  protected final Set<R> rooms = new HashSet<>();

  protected final Set<T> playersInQueue = new HashSet<>();

  protected abstract T constructPlayer(Principal principal, Consumer<GenericPacket> sendPacket);
  protected abstract R constructRoom(Consumer<GenericPacket> sendPacket, Set<T> players, RoomConfig config);

  public void registerPlayer(@NonNull Principal principal) {
    T player = constructPlayer(principal, webSocket::send);
    playerHolder.addPlayer(player);

    synchronized (playersInQueue) {
      playersInQueue.add(player);
      //TODO players queue
    }
  }

  private R createNewRoom(Set<T> players) {
    R room = constructRoom(webSocket::send, players, getRoomConfig());

    synchronized (rooms) {
      rooms.add(room);
      room.start();
    }

    return room;
  }

  private RoomConfig prepareRoomConfig() {
    RoomConfig config = new RoomConfig();
    config.setMaxPlayers(roomMaxPlayers);
    config.setUpdateSendingInterval(roomUpdatePacketInterval);
    config.setRoomUpdateDestination(roomUpdateDestination);
    return config;
  }
}
