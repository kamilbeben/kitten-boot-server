package kittenserver.required;

import kittenserver.events.WebSocketWrapper;
import kittenserver.example.ExampleLobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.MessageMapping;

import java.security.Principal;

/**
 * @param <T> your implementation of {@link AbstractPlayer}
 * @param <R> your's implementation of {@link AbstractRoom}
 *
 * @see ExampleLobbyService
 */
public abstract class AbstractGameController<T extends AbstractPlayer<R>, R extends AbstractRoom<T>> {

  @Autowired protected WebSocketWrapper webSocket;
  @Autowired protected AbstractLobbyService<T, R> lobbyService;

  @Value("${game.join.lobby.response.path}")
  private String joinLobbyResponsePath;

  @MessageMapping("${game.join.lobby.path}")
  public void joinLobby(Principal principal) {
    lobbyService.registerPlayer(principal);
  }

//
//  @MessageMapping("/message_me_back_manually")
//  @SendPacket
//  public GenericPacket sendToOneManually(TestMessagePacket packet, Principal principal) {
//    packet.setName("manually");
//    return buildPacket(
//      principal,
//      "/message_me_back_manually_response",
//      packet
//    );
//  }
}
