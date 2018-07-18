package kittenserver.config;

import kittenserver.required.AbstractRoom;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RoomConfig {

  /**
   * maximum amount of players in one room
   */
  private int maxPlayers = 4;

  /**
   * Update packet will be send every interval (in milliseconds).
   * @see AbstractRoom#notifyPlayersTimer
   * @see AbstractRoom#notifyPlayers()
   * @see AbstractRoom#buildUpdatePacket()
   */
  private long updateSendingInterval = 100;

  /**
   * Room update packet destination. Must contain {@literal "/uuid"} somewhere
   */
  private String roomUpdateDestination = "/room/update/uuid";

  public String getRoomUpdateDetination(String uuid) {
    return roomUpdateDestination.replace("uuid", uuid);
  }

}
