package org.kittenboot.server.abstracted;

import org.kittenboot.server.properties.RoomProperties;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.*;

import static java.util.Objects.requireNonNull;
import static org.kittenboot.server.packets.GenericPacket.buildPacket;
import static org.kittenboot.server.utils.Destinations.*;
import static org.kittenboot.server.utils.Destinations.PLAYER_LEFT_ROOM;
import static org.kittenboot.server.utils.Destinations.ROOM_UPDATE;

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
   * Method called every loop.
   * @param deltaTime time elapsed from last simulation.
   */
  protected abstract void update (long deltaTime);

  /**
   * Build a packet that is dispatched to every player in the room once every {@link RoomProperties#updatePacketInterval}
   *
   * @see #sendRoomUpdate()
   */
  protected abstract Object buildUpdatePacket();

  protected abstract Object buildPlayerJoinedPacket(T player);

  protected abstract Object buildPlayerLeftPacket(T player);

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
        sendRoomUpdate();
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
    onPlayerLeft(player);
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

    onPlayerJoined(player);
  }

  /**
   * Sends update room packet every {@link RoomProperties#updatePacketInterval}.<br>
   * Called by: {@link #notifyPlayersTimer}
   */
  protected void sendRoomUpdate() {
    notifyPlayers(ROOM_UPDATE, buildUpdatePacket());
  }

  protected void notifyPlayers(String destination, Object payload) {
    if (payload == null) return;

    synchronized (players) {
      players.forEach(player -> player.send(destination, payload));
    }
  }

  protected void stopAfterNextStep() {
    stopped = true;
  }

  protected void onPlayerLeft(T player) {
    notifyPlayers(PLAYER_LEFT_ROOM, buildPlayerLeftPacket(player));
  }

  protected void onPlayerJoined(T player) {
    notifyPlayers(PLAYER_JOINED_ROOM, buildPlayerJoinedPacket(player));
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
