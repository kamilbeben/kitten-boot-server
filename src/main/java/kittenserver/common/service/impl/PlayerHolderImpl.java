package kittenserver.common.service.impl;

import kittenserver.Player;
import kittenserver.common.service.PlayerHolder;

import java.util.*;

import static java.util.Objects.requireNonNull;

public class PlayerHolderImpl implements PlayerHolder {

  private Map<String, Player> map = new HashMap<>();

  @Override
  public void addPlayer(Player player) {
    requireNonNull(player, "Player cannot be null");
    requireNonNull(player.getUUID(), "Player's UUID must be already assigned");

    synchronized (map) {
      map.put(player.getUUID(), player);
    }
  }

  @Override
  public void removePlayer(Player player) {
    requireNonNull(player);
    requireNonNull(player.getRoom());

    player.getRoom().remove(player);

    synchronized (map) {
      map.remove(player);
    }

  }

  @Override
  public Player getPlayerByUUID(String UUID) {
    synchronized (map) {
      return map.get(UUID);
    }
  }
}
