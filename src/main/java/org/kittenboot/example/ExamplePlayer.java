package org.kittenboot.example;

import org.kittenboot.kittenserver.abstracted.AbstractPlayer;
import org.kittenboot.kittenserver.packets.GenericPacket;

import java.security.Principal;
import java.util.function.Consumer;

public class ExamplePlayer extends AbstractPlayer<ExampleRoom> {

  public ExamplePlayer(Principal principal, Consumer<GenericPacket> sendPacket) {
    super(principal, sendPacket);
  }
}
