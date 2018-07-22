package kittenserver.example;

import kittenserver.properties.RoomProperties;
import kittenserver.packets.GenericPacket;
import kittenserver.required.AbstractLobbyService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Set;
import java.util.function.Consumer;

/**
 * In order to inject your own implementation of {@link AbstractLobbyService},
 * use <b>{@link Primary}</b> annotation.
 */
@Component
public class ExampleLobbyService extends AbstractLobbyService<ExamplePlayer, ExampleRoom> {

  @Override
  protected ExamplePlayer constructPlayer(Principal principal, Consumer<GenericPacket> sendPacket) {
    ExamplePlayer player = new ExamplePlayer(principal, sendPacket);
    return player;
  }

  @Override
  protected ExampleRoom constructRoom(Set<ExamplePlayer> players, RoomProperties config) {
    return new ExampleRoom(players, config);
  }

}
