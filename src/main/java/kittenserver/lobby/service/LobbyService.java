package kittenserver.lobby.service;

import kittenserver.lobby.dto.PlayerDto;
import kittenserver.lobby.packets.RegisterPacket;

public interface LobbyService {

  PlayerDto registerPlayer(RegisterPacket packet);

}
