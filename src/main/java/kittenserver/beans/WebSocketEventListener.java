package kittenserver.beans;

import kittenserver.events.PlayerDisconnectEvent;
import kittenserver.events.PlayerJoinEvent;
import kittenserver.required.AbstractPlayer;
import kittenserver.required.AbstractPlayerHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

  @Autowired
  private ApplicationEventPublisher publisher;

  @Autowired
  private AbstractPlayerHolder playerHolder;

  @EventListener(SessionConnectedEvent.class)
  public void onSessionConnected(SessionConnectedEvent event) {
    publisher.publishEvent(new PlayerJoinEvent(event.getUser()));
  }

  @EventListener(SessionDisconnectEvent.class)
  public void onSessionDisconnect(SessionDisconnectEvent event) {
    AbstractPlayer player = playerHolder.getPlayer(event.getUser());

    if (player == null) return;

    publisher.publishEvent(new PlayerDisconnectEvent(player));
  }
}
