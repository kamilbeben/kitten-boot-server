package kittenserver.common.service;

import kittenserver.Player;

import java.util.Set;

public interface PlayerHolder {

  void addPlayer(Player player);

  void removePlayer(Player player);

  Player getPlayerByUUID(String UUID);

}
