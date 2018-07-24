package pl.kamilbeben.kittenserver.required;

import pl.kamilbeben.kittenserver.config.UUIDHandshakeHandler;
import pl.kamilbeben.kittenserver.properties.WebSocketProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Just extend this class and let it autoconfiguration do it's job. Don't forget about these two annotations:
 * <br>
 * <ul>
 *   <li>{@literal @}Configuration</li>
 *   <li>{@literal @}EnableWebSocketMessageBroker</li>
 * </ul>
 * <br>
 * And followed properties in your configuration (application.properties/yml) file:
 * <ul>
 *   <li> socket.register-path </li>
 *   <li> socket.allowed-origins </li>
 *   <li> socket.message-broker-prefix </li>
 *   <li> socket.application-destination-prefix </li>
 * </ul>
 */
@Configuration
@EnableWebSocketMessageBroker
@EnableConfigurationProperties(WebSocketProperties.class)
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

  @Autowired
  private WebSocketProperties properties;

  @Bean
  public WebSocketEventListener webSocketEventListener() {
    return new WebSocketEventListener();
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry config) {
    config.enableSimpleBroker(properties.getMessageBrokerPrefix());
    config.setApplicationDestinationPrefixes(properties.getApplicationDestinationPrefix());
  }

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry
      .addEndpoint(properties.getRegisterEndpoint())
      .setAllowedOrigins(properties.getAllowedOrigins())
      .setHandshakeHandler(new UUIDHandshakeHandler())
      .withSockJS();
  }

}