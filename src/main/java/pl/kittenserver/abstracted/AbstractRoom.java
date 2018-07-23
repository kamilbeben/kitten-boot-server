package pl.kittenserver.abstracted;

import pl.kittenserver.properties.RoomProperties;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.*;

import static java.util.Objects.requireNonNull;
import static pl.kittenserver.packets.GenericPacket.buildPacket;

@ToString
public abstract class AbstractRoom <T extends AbstractPlayer> implements Runnable {

  @Getter protected final String uuid = UUID.randomUUID().toString();
  protected final Set<T> players;
  protected final RoomProperties config;
  protected final Timer notifyPlayersTimer = new Timer();

  private boolean isPrivate;
  private boolean stopped;
  private boolean started;

  public AbstractRoom(@NonNull Set<T> players,
                      @NonNull RoomProperties config) {

    if (players.size() > config.getMaxPlayers()) {
      throw new UnsupportedOperationException("Players size exceeded maximum size");
    }

    this.config = config;
    this.players = players;
    this.players.forEach(player -> player.setRoom(this));
  }

  /**
   * Method called once before the thread is started.
   * Can be used to initialize physics engine, generate environment, etc.
   */
  protected abstract void init();

  /**
   * Method called <b>after</b> the player is removed from the room.
   */
  protected abstract void onPlayerRemove(T player);

  /**
   * Method called <b>after</b> the player is added to the room.
   */
  protected abstract void onPlayerJoin(T player);

  /**
   * Method called every loop.
   * @param deltaTime time elapsed from last simulation.
   */
  protected abstract void update (long deltaTime);

  /**
   * Build a packet that is dispatched to every player in the room once every {@link RoomProperties#updatePacketInterval}.
   * The packet is dispatched to destination configured in {@link RoomProperties#updateDestination}
   *
   * @see #notifyPlayers()
   */
  protected abstract Object buildUpdatePacket();

  /**
   * Starts the game
   *
   * @see #init()
   */
  public void start() {
    init();

    new Thread(this).start();
    started = true;

    notifyPlayersTimer.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        notifyPlayers();
      }
    }, new Date(), config.getUpdatePacketInterval());
  }

  @Override
  public void run() {
    long timeBefore = System.currentTimeMillis();

    while (true) {
      update((System.currentTimeMillis() - timeBefore));
      timeBefore = System.currentTimeMillis();

      if (stopped) break;
    }

    // TODO notify Lobby service
    notifyPlayersTimer.cancel();
  }

  public void remove(T player) {
    synchronized (players) {
      players.remove(player);
    }
    if (players.isEmpty()) {
      stopAfterNextStep();
    }
    onPlayerRemove(player);
  }

  /**
   * Defines whenever player can join currently running game or not.
   * @return false. By default players cannot join started room.
   */
  public boolean canJoin(T player) {
    return false;
  }

  public void join(T player) throws UnsupportedOperationException {
    if (players.contains(player)) {
      return;
    }

    synchronized (players) {
      players.add(player);
    }

    onPlayerJoin(player);
  }

  /**
   * Sends update room packet every {@link RoomProperties#updatePacketInterval}.<br>
   * Called by: {@link #notifyPlayersTimer}
   */
  protected void notifyPlayers() {
    Object data = buildUpdatePacket();

    synchronized (players) {
      players.forEach(player -> player.send(config.getUpdateDestination(), data));
    }
  }

  protected void stopAfterNextStep() {
    stopped = true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AbstractRoom room = (AbstractRoom) o;
    return Objects.equals(uuid, room.uuid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uuid);
  }
}
