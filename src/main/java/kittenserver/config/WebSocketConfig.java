package kittenserver.config;

import kittenserver.config.UUIDHandshakeHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  public static final String DESTINATION_PREFIX = "/game_get";
  public static final String APPLICATION_DESTINATION_PREFIX = "/game_post";
  public static final String REGISTER_ENDPOINT = "/register";
  public static final String ALLOWED_ORIGINS = "*";

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker(DESTINATION_PREFIX);
    config.setApplicationDestinationPrefixes(APPLICATION_DESTINATION_PREFIX);
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry
      .addEndpoint(REGISTER_ENDPOINT)
      .setAllowedOrigins(ALLOWED_ORIGINS)
      .setHandshakeHandler(new UUIDHandshakeHandler())
      .withSockJS();
  }

}