package org.kittenboot.server.events;

import org.kittenboot.server.abstracted.AbstractPlayer;
import org.springframework.context.ApplicationEvent;

public class PlayerDisconnectEvent extends ApplicationEvent {

  public PlayerDisconnectEvent(AbstractPlayer player) {
    super(player);
  }

}
