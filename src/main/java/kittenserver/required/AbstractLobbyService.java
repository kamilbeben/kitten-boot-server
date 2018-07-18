package kittenserver.required;

import kittenserver.dto.BasePlayerDto;
import kittenserver.packets.RegisterPacket;
import kittenserver.required.defaults.DefaultLobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

/**
 * @param <T> your implementation of {@link AbstractPlayer}
 * @param <R> your's implementation of {@link AbstractRoom}
 *
 * @see DefaultLobbyService
 */
public abstract class AbstractLobbyService<T extends AbstractPlayer<R>, R extends AbstractRoom<T>> {

  @Autowired protected SimpMessagingTemplate webSocket;
  @Autowired protected AbstractPlayerHolder<T> playerHolder;

  protected final Set<AbstractRoom> rooms = new HashSet<>();
  protected long maxAmountOfWaitBeforeStartingARoom = 30 * 1000;

  protected abstract T constructPlayer(RegisterPacket packet, Principal principal);
  protected abstract R constructRoom(Set<T> players, SimpMessagingTemplate messagingTemplate);

  public BasePlayerDto registerPlayer(RegisterPacket packet, Principal principal) {
    T player = constructPlayer(packet, principal);
    playerHolder.addPlayer(player);
    return new BasePlayerDto(player);
  }

  private R createNewRoom(Set<T> players) {
    R room = constructRoom(players, webSocket);

    synchronized (rooms) {
      rooms.add(room);
      room.start();
    }

    return room;
  }

}
