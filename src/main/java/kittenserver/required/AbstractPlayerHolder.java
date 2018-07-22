package kittenserver.required;

import kittenserver.example.ExamplePlayerHolder;
import lombok.NonNull;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.requireNonNull;

/**
 * This class holds every player instance in {@link AbstractPlayerHolder#map}.<br>
 * It is important to keep it <b>singletone</b>, which is default bean type in Spring, so don't change it!<br>
 * @param <T> your's implementation of {@link AbstractPlayer}
 *
 * @see ExamplePlayerHolder
 */
public abstract class AbstractPlayerHolder<T extends AbstractPlayer> {

  protected Map<Principal, T> map = new HashMap<>();

  public void addPlayer(@NonNull T player) {
    synchronized (map) {
      map.put(player.getPrincipal(), player);
    }
  }

  public void removePlayer(@NonNull T player) {
    requireNonNull(player.getRoom());

    player.getRoom().remove(player);

    synchronized (map) {
      map.remove(player.getPrincipal());
    }
  }

  /**
   *
   * @param principal Spring security Principal
   * @return Player if {@link #map} contains Player associated with given Principal, otherwise {@literal null}.
   */
  public T getPlayer(@NonNull Principal principal) {
    synchronized (map) {
      return map.get(principal);
    }
  }
}
