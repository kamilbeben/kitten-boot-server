package kittenserver.required.defaults;

import kittenserver.required.AbstractPlayer;

import java.security.Principal;

public class DefaultPlayer extends AbstractPlayer<DefaultRoom> {

  public DefaultPlayer(Principal principal) {
    super(principal);
  }
}
