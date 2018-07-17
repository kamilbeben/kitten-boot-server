package kittenserver.lobby;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MessagePacket {

  private Long id;
  private String name;
  private String content;

}
