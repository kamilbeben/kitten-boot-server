package kittenserver.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

  @Value("${socket.register.path}")
  private String registerEndpoint;

  @Value("${socket.allowed.origins}")
  private String allowedOrigins;

  @Value("${socket.message.broker.prefix}")
  private String messageBrokerPrefix;

  @Value("${socket.application.destination.prefix}")
  private String applicationDestinationPrefix;

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker(messageBrokerPrefix);
    config.setApplicationDestinationPrefixes(applicationDestinationPrefix);
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry
      .addEndpoint(registerEndpoint)
      .setAllowedOrigins(allowedOrigins)
      .setHandshakeHandler(new UUIDHandshakeHandler())
      .withSockJS();
  }
}