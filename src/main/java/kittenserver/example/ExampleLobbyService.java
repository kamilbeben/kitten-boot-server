package kittenserver.example;

import kittenserver.config.RoomConfig;
import kittenserver.packets.GenericPacket;
import kittenserver.required.AbstractLobbyService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Set;
import java.util.function.Consumer;

/**
 * In order to inject your own implementation of {@link AbstractLobbyService},
 * use <b>{@link Primary}</b> annotation.
 */
@Service
public class ExampleLobbyService extends AbstractLobbyService<ExamplePlayer, ExampleRoom> {

  private int counter = 0;

  @Override
  protected ExamplePlayer constructPlayer(Principal principal, Consumer<GenericPacket> sendPacket) {
    ExamplePlayer player = new ExamplePlayer(principal, sendPacket, generateNextName());
    sendNameGeneratedPacket(player);
    return player;
  }

  public void sendNameGeneratedPacket(ExamplePlayer player) {
    webSocket.send(new NameGeneratedPacket(player));
  }

  @Override
  protected ExampleRoom constructRoom(Consumer<GenericPacket> sendPacket, Set<ExamplePlayer> players, RoomConfig config) {
    return new ExampleRoom(sendPacket, players, config);
  }

  private synchronized String generateNextName() {
    return "player_" + counter++;
  }
}
