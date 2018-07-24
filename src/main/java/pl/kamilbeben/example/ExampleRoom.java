package pl.kamilbeben.example;

import pl.kamilbeben.kittenserver.abstracted.AbstractRoom;
import pl.kamilbeben.kittenserver.properties.RoomProperties;

import java.util.Set;

public class ExampleRoom extends AbstractRoom<ExamplePlayer> {

  public ExampleRoom(Set<ExamplePlayer> players, RoomProperties config) {
    super(players, config);
  }

  @Override
  protected void init() {
    synchronized (players) {

      // that's a perfect place to setup player body, health or whatever you need

      // let's inform them that the room has started.
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
    // this is the right place for broadcasting data about every player's position, etc.
    RoomUpdate packet = new RoomUpdate();
    packet.setSomeData("literally anything");
    return packet;
  }
}
