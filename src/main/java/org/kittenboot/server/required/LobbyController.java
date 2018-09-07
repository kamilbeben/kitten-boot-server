package org.kittenboot.server.required;

import org.kittenboot.server.abstracted.AbstractLobbyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

import static org.kittenboot.server.utils.Destinations.JOIN_PRIVATE_ROOM;
import static org.kittenboot.server.utils.Destinations.JOIN_PUBLIC_QUEUE;
import static org.kittenboot.server.utils.Destinations.START_PRIVATE_ROOM;

@RestController
public class LobbyController {

  @Autowired
  private AbstractLobbyService lobbyService;

  @MessageMapping(JOIN_PUBLIC_QUEUE)
  public void joinPublicQueue(Principal principal) {
    lobbyService.joinPublicQueue(principal);
  }

  @MessageMapping(START_PRIVATE_ROOM)
  public void startPrivateRoom(Principal principal) {
    lobbyService.createPrivateQueue(principal);
  }

  @MessageMapping(JOIN_PRIVATE_ROOM)
  public void joinPrivateQueue(String queueId, Principal principal) {
    lobbyService.joinPrivateQueue(queueId, principal);
  }
}
