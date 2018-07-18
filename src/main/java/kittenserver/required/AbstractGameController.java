package kittenserver.required;

import kittenserver.TestMessagePacket;
import kittenserver.dto.BasePlayerDto;
import kittenserver.packets.RegisterPacket;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class AbstractGameController {

  private final SimpMessagingTemplate messagingTemplate;
  private final AbstractLobbyService lobbyService;

  @MessageMapping("/join_lobby")
  @SendToUser("/join_lobby_response")
  public BasePlayerDto sendToOne(RegisterPacket packet, Principal principal) {
    return lobbyService.registerPlayer(packet, principal);
  }

//  public void sendToRoom() {
//    websocket.convertAndSend("/lobby/update/5", new Greeting("kek"));
//  }

  @MessageMapping
  public void updateRoom(Principal principal) {

  }

  @MessageMapping("/message")
  @SendTo("/game_get/message_response")
  public TestMessagePacket sendToAll(TestMessagePacket messagePacket, Principal principal) throws Exception {
    messagePacket.setName("test name");
    return messagePacket;
  }

  @MessageMapping("/message_me_back")
  @SendToUser("/game_get/message_me_back_response")
  public TestMessagePacket sendToOne(TestMessagePacket messagePacket, Principal principal) throws Exception {
    messagePacket.setName("test name");
    return messagePacket;
  }

  @MessageMapping("/message_me_back_manually")
  public void sendToOneManually(TestMessagePacket packet, Principal principal) {
    packet.setName("manually");
    messagingTemplate.convertAndSendToUser(principal.getName(), "/game_get/message_me_back_manually_response", packet);
  }

}
