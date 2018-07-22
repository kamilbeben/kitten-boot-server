package kittenserver.required;

import kittenserver.config.RoomConfig;
import kittenserver.packets.GenericPacket;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.*;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;
import static kittenserver.packets.GenericPacket.buildPacket;

@ToString
public abstract class AbstractRoom <T extends AbstractPlayer> implements Runnable {

  @Getter protected final String uuid = UUID.randomUUID().toString();
  protected final Set<T> players;
  protected final Consumer<GenericPacket> sendPacket;
  protected final RoomConfig config;
  protected final Timer notifyPlayersTimer = new Timer();
  protected final String updateDestination;

  private boolean stopRoom = false;

  public AbstractRoom(@NonNull Consumer<GenericPacket> sendPacket,
                      @NonNull Set<T> players,
                      @NonNull RoomConfig config) {

    if (players.size() > config.getMaxPlayers()) {
      throw new UnsupportedOperationException("Players size exceeded maximum size");
    }

    this.sendPacket = sendPacket;
    this.config = config;
    this.players = players;

    this.updateDestination = config.getRoomUpdateDetination(uuid);
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
   * Build a packet that is dispatched to every player in the room once every {@link RoomConfig#updateSendingInterval}.
   * The packet is dispatched to destination under {@link #updateDestination}.
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
    long timeBefore = System.currentTimeMillis();

    while (true) {
      update((System.currentTimeMillis() - timeBefore));
      timeBefore = System.currentTimeMillis();

      if (stopRoom) {
        break;
      }
    }

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
   */
  public boolean canJoin(T player) {
    return false;
  }

  public void join(T player) throws UnsupportedOperationException {
    if (players.contains(player)) {
      throw new UnsupportedOperationException(
        "Player already in room.\n" +
        "Player: [" + player + "]\n" +
        "Players: [" + Arrays.toString(players.toArray()) + "]"
      );
    }
    synchronized (players) {
      players.add(player);
    }
    onPlayerJoin(player);
  }

  /**
   * Sends update room packet every {@link RoomConfig#updateSendingInterval}.<br>
   * Called by: {@link #notifyPlayersTimer}
   */
  protected void notifyPlayers() {
    sendPacket.accept(
      buildPacket(
        updateDestination,
        buildUpdatePacket()
      )
    );
  }

  protected void stopAfterNextStep() {
    this.stopRoom = true;
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
