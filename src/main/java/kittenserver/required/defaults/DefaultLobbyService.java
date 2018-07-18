package kittenserver.required.defaults;

import kittenserver.config.RoomConfig;
import kittenserver.packets.RegisterPacket;
import kittenserver.required.AbstractLobbyService;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Set;

/**
 * In order to inject your own implementation of {@link AbstractLobbyService},
 * use <b>{@link Primary}</b> annotation.
 */
@Service
public class DefaultLobbyService extends AbstractLobbyService<DefaultPlayer, DefaultRoom> {

  private int counter = 0;

  @Override
  protected DefaultPlayer constructPlayer(RegisterPacket packet, Principal principal) {
    DefaultPlayer player = new DefaultPlayer(principal);
    player.setName("player_" + incrementCounter());
    return player;
  }

  @Override
  protected DefaultRoom constructRoom(Set<DefaultPlayer> players, SimpMessagingTemplate messagingTemplate) {
    return new DefaultRoom(messagingTemplate, players, new RoomConfig());
  }

  private synchronized int incrementCounter() {
    return ++counter;
  }
}
