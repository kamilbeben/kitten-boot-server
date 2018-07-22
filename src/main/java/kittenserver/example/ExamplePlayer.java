package kittenserver.example;

import kittenserver.packets.GenericPacket;
import kittenserver.required.AbstractPlayer;
import lombok.Getter;
import lombok.Setter;

import java.security.Principal;
import java.util.function.Consumer;

public class ExamplePlayer extends AbstractPlayer<ExampleRoom> {

  /**
   * Player's nickname.
   */
  @Getter
  protected String name;

  public ExamplePlayer(Principal principal, Consumer<GenericPacket> sendPacket, String name) {
    super(principal, sendPacket);
    this.name = name;
  }
}
