package pl.kamilbeben.kittenserver.required;

import pl.kamilbeben.kittenserver.abstracted.AbstractRoom;
import pl.kamilbeben.kittenserver.events.PlayerDisconnectEvent;
import pl.kamilbeben.kittenserver.abstracted.AbstractPlayer;
import lombok.NonNull;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

/**
 * This class holds every player instance in {@link PlayerHolder#map}.
 */
@Component
@Scope(SCOPE_SINGLETON)
public class PlayerHolder {

  protected Map<Principal, AbstractPlayer> map = new HashMap<>();

  public void addPlayer(@NonNull AbstractPlayer player) {
    synchronized (map) {
      map.put(player.getPrincipal(), player);
    }
  }

  @EventListener(PlayerDisconnectEvent.class)
  public void onPlayerDisconnect(PlayerDisconnectEvent event) {
    AbstractPlayer player = (AbstractPlayer) event.getSource();

    synchronized (map) {
      map.remove(player.getPrincipal());
    }

    // remove player from room if already in one
    AbstractRoom room = player.getRoom();
    if (room != null) {
      room.remove(player);
    }
  }

  /**
   *
   * @param principal Spring security Principal
   * @return Player if {@link #map} contains Player associated with given Principal, otherwise {@literal null}.
   */
  public AbstractPlayer getPlayer(@NonNull Principal principal) {
    synchronized (map) {
      return map.get(principal);
    }
  }

}
