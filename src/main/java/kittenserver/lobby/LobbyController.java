package kittenserver.lobby;

import kittenserver.lobby.dto.PlayerDto;
import kittenserver.lobby.packets.RegisterPacket;
import kittenserver.lobby.service.LobbyService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class LobbyController {

  private final SimpMessagingTemplate messagingTemplate;
  private final LobbyService lobbyService;

  private static long cnt = 0;

//  public void sendToRoom() {
//    messagingTemplate.convertAndSend("/lobby/update/5", new Greeting("kek"));
//  }

  @MessageMapping("/join")
  @SendToUser("/lobby/join")
  public PlayerDto sendToOne(RegisterPacket packet) {
    return lobbyService.registerPlayer(packet);
  }

//  @MessageMapping("/message")
//  @SendTo("/lobby/message")
//  public MessagePacket sendToAll(MessagePacket messagePacket) throws Exception {
//    messagePacket.setName("test");
//    return messagePacket;
//  }

}
