package org.kittenboot.server.properties;

import org.kittenboot.server.abstracted.AbstractRoom;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ToString
@ConfigurationProperties("room")
public class RoomProperties {

  /**
   * minimum amount of players before starting a room
   */
  private int minPlayers = 2;

  /**
   * maximum amount of players in one room
   */
  private int maxPlayers = 4;

  /**
   * maximum amount of time to wait before starting a room (if the amount of players in queue is equal or more than {@link #minPlayers})
   */
  private long maximumWaitingTime = 30 * 1000;

  /**
   * Update packet will be send every interval (in milliseconds).
   *
   * @see AbstractRoom#sendRoomUpdate()
   * @see AbstractRoom#buildUpdatePacket()
   */
  private long updatePacketInterval = 33;

}
