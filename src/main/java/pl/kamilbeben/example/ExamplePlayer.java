package pl.kamilbeben.example;

import pl.kamilbeben.kittenserver.packets.GenericPacket;
import pl.kamilbeben.kittenserver.abstracted.AbstractPlayer;

import java.security.Principal;
import java.util.function.Consumer;

public class ExamplePlayer extends AbstractPlayer<ExampleRoom> {

  public ExamplePlayer(Principal principal, Consumer<GenericPacket> sendPacket) {
    super(principal, sendPacket);
  }
}
