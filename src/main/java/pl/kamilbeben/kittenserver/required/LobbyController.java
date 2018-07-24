package pl.kamilbeben.kittenserver.required;

import pl.kamilbeben.kittenserver.abstracted.AbstractLobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class LobbyController {

  @Autowired
  private AbstractLobbyService lobbyService;

  @MessageMapping("/join_public_queue")
  public void joinPublicQueue(Principal principal) {
    lobbyService.joinPublicQueue(principal);
  }

  @MessageMapping("/start_private_room")
  public void startPrivateRoom(Principal principal) {
    // TODO impl
  }

  @MessageMapping("/join_private_room")
  public void joinPrivateRoom(String roomUuid, Principal principal) {
    // TODO impl
  }
}
