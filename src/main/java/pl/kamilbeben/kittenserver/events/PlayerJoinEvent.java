package pl.kamilbeben.kittenserver.events;

import org.springframework.context.ApplicationEvent;

import java.security.Principal;

public class PlayerJoinEvent extends ApplicationEvent {

  public PlayerJoinEvent(Principal principal) {
    super(principal);
  }

  @Override
  public Principal getSource() {
    return (Principal) super.getSource();
  }
}
