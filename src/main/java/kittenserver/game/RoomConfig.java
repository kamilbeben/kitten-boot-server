package kittenserver.game;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@ToString
public class RoomConfig {

  private int maxPlayers;
  private int rounds;
  private long updateSendingInterval;

}
