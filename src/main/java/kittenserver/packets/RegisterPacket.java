package kittenserver.packets;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterPacket {

  private String uuid;
  private String name;

}
