package kittenserver.lobby.dto;

import kittenserver.Player;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PlayerDto {

  private String UUID;
  private String name;
  private String roomUUID;

  public PlayerDto(Player player) {
    setUUID(player.getUUID());
    setName(player.getName());
    setRoomUUID(player.getRoom().getUUID());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PlayerDto that = (PlayerDto) o;
    return Objects.equals(UUID, that.UUID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(UUID);
  }
}
