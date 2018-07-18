package kittenserver.required;

import kittenserver.required.defaults.DefaultPlayerHolder;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * This class holds every player instance in {@link AbstractPlayerHolder#map}.<br>
 * It is important to keep it <b>singletone</b>, which is default bean type in Spring, so don't change it!<br>
 * @param <T> your's implementation of {@link AbstractPlayer}
 *
 * @see DefaultPlayerHolder
 */
public abstract class AbstractPlayerHolder<T extends AbstractPlayer> {

  protected Map<Principal, T> map = new HashMap<>();

  public void addPlayer(T player) {
    requireNonNull(player, "Player cannot be null");

    synchronized (map) {
      map.put(player.getPrincipal(), player);
    }
  }

  public void removePlayer(T player) {
    requireNonNull(player);
    requireNonNull(player.getRoom());

    player.getRoom().remove(player);

    synchronized (map) {
      map.remove(player.getPrincipal());
    }

  }

  public T getPlayerByPrincipal(String UUID) {
    synchronized (map) {
      return map.get(UUID);
    }
  }
}
