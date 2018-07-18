package kittenserver.dto;

import kittenserver.required.AbstractPlayer;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class BasePlayerDto {

  protected String uuid;
  protected String name;

  public BasePlayerDto(AbstractPlayer player) {
    setUuid(player.getUuid());
    setName(player.getName());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BasePlayerDto that = (BasePlayerDto) o;
    return Objects.equals(uuid, that.uuid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uuid);
  }
}
