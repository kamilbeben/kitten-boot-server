package kittenserver.example;

import kittenserver.config.RoomConfig;
import kittenserver.packets.GenericPacket;
import kittenserver.required.AbstractRoom;

import java.util.Set;
import java.util.function.Consumer;

public class ExampleRoom extends AbstractRoom<ExamplePlayer> {

  public ExampleRoom(Consumer<GenericPacket> sendPacket, Set<ExamplePlayer> players, RoomConfig config) {
    super(sendPacket, players, config);
  }

  @Override
  protected void init() {

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
    return null;
  }
}
