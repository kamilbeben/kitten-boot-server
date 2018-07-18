package kittenserver.required.defaults;

import kittenserver.config.RoomConfig;
import kittenserver.required.AbstractRoom;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.Set;

public class DefaultRoom extends AbstractRoom<DefaultPlayer> {

  public DefaultRoom(SimpMessagingTemplate messagingTemplate, Set<DefaultPlayer> players, RoomConfig config) {
    super(messagingTemplate, players, config);
  }

  @Override
  protected void init() {

  }

  @Override
  protected void onPlayerRemove(DefaultPlayer player) {

  }

  @Override
  protected void onPlayerJoin(DefaultPlayer player) {

  }

  @Override
  protected void update(long deltaTime) {

  }

  @Override
  protected Object buildUpdatePacket() {
    return null;
  }
}
