package pl.example;

import pl.kittenserver.packets.GenericPacket;
import pl.kittenserver.abstracted.AbstractPlayer;

import java.security.Principal;
import java.util.function.Consumer;

public class ExamplePlayer extends AbstractPlayer<ExampleRoom> {

  public ExamplePlayer(Principal principal, Consumer<GenericPacket> sendPacket) {
    super(principal, sendPacket);
  }
}
