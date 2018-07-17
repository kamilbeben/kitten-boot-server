package kittenserver.game;

import kittenserver.Player;
import lombok.Getter;
import lombok.ToString;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.Objects.requireNonNull;

@ToString
public abstract class Room extends Runnable {

  @Getter protected final String uuid = UUID.randomUUID().toString();
  protected Set<Player> players;
  protected RoomConfig config;

  private Timer notifyPlayersTimer = new Timer();
  private SimpMessagingTemplate messagingTemplate;

  public Room(SimpMessagingTemplate messagingTemplate, Set<Player> players, RoomConfig config) {
    requireNonNull(players);
    requireNonNull(config);
    requireNonNull(messagingTemplate);

    if (players.size() > config.getMaxPlayers()) {
      throw new UnsupportedOperationException("Players size exceeded maximum size");
    }

    this.messagingTemplate = messagingTemplate;
    this.config = config;
    this.players = players;
  }

  protected abstract void init();
  protected abstract void onPlayerRemove(Player player);
  protected abstract boolean update (long deltaTime);
  protected abstract Object buildUpdatePacket();

  public void start() {
    init();

    new Thread(this).run();

    notifyPlayersTimer.schedule(new TimerTask() {
      @Override
      public void run() {
        notifyPlayers();
      }
    }, config.getUpdateSendingInterval());
  }

  @Override
  public void run() {
    long timeBeforeLoop = System.currentTimeMillis();
    AtomicBoolean end = new AtomicBoolean(false);

    while (!end.get()) {
      end.set(players.isEmpty() || update(System.currentTimeMillis() - timeBeforeLoop));
    }

    notifyPlayersTimer.cancel();
  }

  private void notifyPlayers() {
    this.messagingTemplate.convertAndSend("/room/update/" + this.uuid, buildUpdatePacket());
  }

  public void remove(Player player) {
    players.remove(player);
    onPlayerRemove(player);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Room room = (Room) o;
    return Objects.equals(UUID, room.UUID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(UUID);
  }
}
