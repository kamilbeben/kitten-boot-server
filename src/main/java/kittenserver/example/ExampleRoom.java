package kittenserver.example;

import kittenserver.properties.RoomProperties;
import kittenserver.required.AbstractRoom;

import java.util.Set;

public class ExampleRoom extends AbstractRoom<ExamplePlayer> {

  public ExampleRoom(Set<ExamplePlayer> players, RoomProperties config) {
    super(players, config);
  }

  @Override
  protected void init() {
    synchronized (players) {
      players.forEach(player -> player.send("/room_started", "ROOM STARTED"));
    }
  }

  @Override
  protected void onPlayerRemove(ExamplePlayer player) {

  }

  @Override
  protected void onPlayerJoin(ExamplePlayer player) {

  }

  @Override
  protected void update(long deltaTime) {

  }

  @Override
  protected Object buildUpdatePacket() {
    RoomUpdate packet = new RoomUpdate();
    packet.setSomeData("literally anything");
    return packet;
  }
}
