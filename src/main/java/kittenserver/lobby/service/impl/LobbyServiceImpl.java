package kittenserver.lobby.service.impl;

import kittenserver.Player;
import kittenserver.game.Room;
import kittenserver.game.RoomConfig;
import kittenserver.lobby.dto.PlayerDto;
import kittenserver.lobby.packets.RegisterPacket;
import kittenserver.lobby.service.LobbyService;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.springframework.util.StringUtils.isEmpty;

@Service
public class LobbyServiceImpl implements LobbyService {

  private int counter;

  private Set<Room> rooms = new HashSet<>();

  @Override
  public PlayerDto registerPlayer(RegisterPacket packet) {
    Player player = new Player();

    if (isEmpty(packet.getName())) {
      player.setName("kitten_" + increment());
    }
    player.setUUID(UUID.randomUUID().toString());

    return new PlayerDto(player);
  }

  private synchronized int increment() {
    return ++counter;
  }

  private Room createNewRoom(Set<Player> players) {
    Room room = new Room(
      players,
      RoomConfig.builder()
        .maxPlayers(4)
        .updateSendingInterval(33)
        .rounds(10)
        .build()
    );

    synchronized (rooms) {
      rooms.add(room);
      room.start();
    }

    return room;
  }

}
