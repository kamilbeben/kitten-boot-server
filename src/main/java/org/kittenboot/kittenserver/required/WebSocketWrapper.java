package org.kittenboot.kittenserver.required;

import org.kittenboot.kittenserver.packets.GenericPacket;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebSocketWrapper {

  private final SimpMessagingTemplate webSocket;

  @Value("${socket.message-broker-prefix}")
  private String destinationPrefix;

  public void send(GenericPacket packet) {

    // broadcast
    if (packet.getPrincipal() == null) {
      webSocket.convertAndSend(
        destinationPrefix + packet.getDestination(),
        packet.getData()
      );
    }
    // send to user
    else {
      webSocket.convertAndSendToUser(
        packet.getPrincipal().getName(),
        destinationPrefix + packet.getDestination(),
        packet.getData()
      );
    }
  }

}
