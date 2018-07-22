package kittenserver.example;

import kittenserver.required.AbstractGameController;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExampleGameController extends AbstractGameController<ExamplePlayer, ExampleRoom> {

//  @MessageMapping("/message")
//  @SendPacket
//  public GenericPacket<TestMessagePacket> sendToAll(TestMessagePacket data) {
//    data.setName("test name");
//    return buildPacket(
//      "/message_response",
//      data
//    );
//  }

}
