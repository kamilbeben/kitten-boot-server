package org.kittenboot.server.abstracted;

import org.kittenboot.server.events.PlayerDisconnectEvent;
import org.kittenboot.server.events.PlayerJoinEvent;
import org.kittenboot.server.packets.GenericPacket;
import org.kittenboot.server.required.WebSocketWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.kittenboot.server.required.PlayerHolder;
import org.kittenboot.server.properties.RoomProperties;

import java.security.Principal;
import java.util.*;
import java.util.function.Consumer;

import static org.kittenboot.server.packets.GenericPacket.*;
import static org.kittenboot.server.utils.Destinations.*;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_SINGLETON;

/**
 * @param <T> your implementation of {@link AbstractPlayer}
 * @param <R> your's implementation of {@link AbstractRoom}
 */
@Scope(SCOPE_SINGLETON)
@EnableConfigurationProperties(RoomProperties.class)
public abstract class AbstractLobbyService<T extends AbstractPlayer<R>, R extends AbstractRoom<T>> {

  @Autowired protected WebSocketWrapper webSocket;
  @Autowired protected PlayerHolder playerHolder;
  @Autowired protected RoomProperties roomProperties;

  // rooms
  protected final Set<R> rooms = new HashSet<>();

  // queues
  protected final Set<T> publicQueue = new HashSet<>();
  protected final Map<String, Set<T>> privateQueues = new HashMap<>();

  // abstract methods
  protected abstract T constructPlayer(Principal principal, Consumer<GenericPacket> sendPacket);
  protected abstract R constructRoom(Set<T> players, RoomProperties config);
  protected abstract Object buildPlayerJoinedQueuePacket(T player);
  protected abstract Object buildPlayerLeftQueue(T player);
  protected abstract Object buildJoinedQueuePacket(T player, Set<T> queue);


  // event listeners
  @EventListener(PlayerJoinEvent.class)
  public void registerPlayer(PlayerJoinEvent event) {
    T player = constructPlayer(event.getSource(), webSocket::send);
    playerHolder.addPlayer(player);
  }

  @EventListener(PlayerDisconnectEvent.class)
  public void removePlayer(PlayerDisconnectEvent event) {
    T player = (T) event.getSource();

    // remove player from room
    if (player.isInRoom()) {
      AbstractRoom room = player.getRoom();
      room.remove(player);
      return;
    }

    // public queue
    if (player.isInPublicQueue()) {
      synchronized (publicQueue) {
        publicQueue.remove(player);
        notifyQueue(publicQueue, PLAYER_LEFT_QUEUE, buildPlayerLeftQueue(player));
      }
      return;
    }

    // or private queue
    if (player.isInPrivateQueue()) {
      synchronized (privateQueues) {
        Set<T> queue = privateQueues.get(player.getPrivateQueueId());
        if (queue != null) {
          queue.remove(player);
          notifyQueue(queue, PLAYER_LEFT_QUEUE, buildPlayerLeftQueue(player));
        }
      }
      return;
    }
  }

  public void joinPublicQueue(Principal principal) {
    T player = getPlayer(principal);
    if (player.getRoom() != null) return;

    synchronized (publicQueue) {
      notifyQueue(publicQueue, PLAYER_JOINED_QUEUE, buildPlayerJoinedQueuePacket(player));
      publicQueue.add(player);
      player.send(JOINED_QUEUE, buildJoinedQueuePacket(player, publicQueue));
      refreshQueue();
    }
  }

  public void createPrivateQueue(Principal principal) {
    T player = getPlayer(principal);

    synchronized (privateQueues) {
      String queueId = UUID.randomUUID().toString();
      Set<T> queue = new HashSet<>();
      queue.add(player);

      privateQueues.put(queueId, queue);
      player.send(QUEUE_CREATED, queueId);
    }
  }

  public void joinPrivateQueue(String queueId, Principal principal) {
    T player = getPlayer(principal);

    synchronized (privateQueues) {
      if (!privateQueues.containsKey(queueId)) {

        player.send(QUEUE_NOT_FOUND, queueId);
      } else {

        Set<T> queue = privateQueues.get(queueId);
        notifyQueue(queue, PLAYER_JOINED_QUEUE, buildPlayerJoinedQueuePacket(player));
        queue.add(player);
        player.send(JOINED_QUEUE, buildJoinedQueuePacket(player, queue));
        refreshPrivateQueue(queue, player);
      }
    }
  }

  protected void refreshPrivateQueue(Set<T> allPlayers, T newPlayer) {

  }

  /**
   * If you need to implement custom queueing or room creation policy, then overriding this method is a good place to start.
   * Keep in mind that it's execution is synchronized on {@link #publicQueue}!
   */
  protected void refreshQueue() {

    int playersCount = publicQueue.size();

    // first player joined queue, start counting
    if (playersCount == 1) {
      // TODO think of something clever
      return;
    }

    // sequent player joined queue, but minimum players limit was not yet reached
    if (playersCount == roomProperties.getMinPlayers()) {
      startNewPublicRoom();
      return;
    }

    // sequent player joined queue, maximum players limit was reached, starting new room
    if (playersCount >= roomProperties.getMaxPlayers()) {
      startNewPublicRoom();
      return;
    }
  }

  protected T getPlayer(Principal principal) {
    AbstractPlayer player = playerHolder.getPlayer(principal);
    return player != null
      ? (T) player
      : null;
  }

  private void notifyQueue(Set<T> queue, String destination, Object payload) {
    synchronized (queue) {
      queue.forEach(player -> player.send(destination, payload));
    }
  }

  private void startNewPublicRoom() {
    HashSet<T> clonedPlayers = new HashSet<>(publicQueue);
    publicQueue.clear();

    R newRoom = createNewRoom(clonedPlayers, false);

    newRoom.start();
  }

  private R createNewRoom(Set<T> players, boolean isPrivate) {
    R room = constructRoom(players, roomProperties);

    rooms.add(room);
    return room;
  }
}
