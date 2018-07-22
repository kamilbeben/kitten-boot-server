package kittenserver.events;

import kittenserver.required.AbstractPlayer;
import org.springframework.context.ApplicationEvent;

public class PlayerDisconnectEvent extends ApplicationEvent {

  public PlayerDisconnectEvent(AbstractPlayer player) {
    super(player);
  }

}
