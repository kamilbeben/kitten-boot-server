package kittenserver.example;

import kittenserver.packets.GenericPacket;

public class NameGeneratedPacket extends GenericPacket<String> {

  public NameGeneratedPacket(ExamplePlayer player) {
    super();

    setDestination("/generate_name");
    setPrincipal(player.getPrincipal());
    setData(player.getName());
  }
}
