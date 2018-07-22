package kittenserver.example;

import kittenserver.packets.GenericPacket;
import kittenserver.required.AbstractPlayer;
import lombok.Getter;
import lombok.Setter;

import java.security.Principal;
import java.util.function.Consumer;

public class ExamplePlayer extends AbstractPlayer<ExampleRoom> {

  public ExamplePlayer(Principal principal, Consumer<GenericPacket> sendPacket) {
    super(principal, sendPacket);
  }
}
