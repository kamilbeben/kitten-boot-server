package kittenserver.required;

import kittenserver.config.WebSocketConfig;
import kittenserver.config.RoomConfig;
import lombok.Getter;
import lombok.ToString;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.*;

import static java.util.Objects.requireNonNull;
import static kittenserver.config.WebSocketConfig.DESTINATION_PREFIX;

@ToString
public abstract class AbstractRoom <T extends AbstractPlayer> implements Runnable {

  @Getter protected final String uuid = UUID.randomUUID().toString();
  protected final Set<T> players;
  protected final RoomConfig config;
  protected final SimpMessagingTemplate webSocket;
  protected final Timer notifyPlayersTimer = new Timer();
  protected final String updateDestination;

  private boolean stopRoom = false;

  public AbstractRoom(SimpMessagingTemplate webSocket, Set<T> players, RoomConfig config) {
    requireNonNull(players);
    requireNonNull(config);
    requireNonNull(webSocket);

    if (players.size() > config.getMaxPlayers()) {
      throw new UnsupportedOperationException("Players size exceeded maximum size");
    }

    this.webSocket = webSocket;
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
   * Sends packet to given player.<br>
   * @param destination packet destination.<br>
   *                    Keep in mind two things:
   *                    <ul>
   *                      <li><b>{@link WebSocketConfig#DESTINATION_PREFIX} + "/"</b> is prepended to the destination parameter</li>
   *                      <li>client-side must also prepend <b> "/user/"</b></li>
   *                    </ul>
   *                    So, assuming that WebSocketConfig.DESTINATION_PREFIX is "/game_get", and destination is "you_died", then
   *                    client-side must subscribe to <b>/user/game_get/you_died</b>.
   */
  protected void send(T player, String destination, Object packet) {
    webSocket.convertAndSendToUser(player.getPrincipal().getName(), DESTINATION_PREFIX + "/" + destination, packet);
  }

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
    this.webSocket.convertAndSend(updateDestination, buildUpdatePacket());
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
