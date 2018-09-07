package org.kittenboot.server.required;

import org.kittenboot.server.config.UUIDHandshakeHandler;
import org.kittenboot.server.properties.WebSocketProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeHandler;

/**
 * If you are using {@link KittenMarker} as a marker for {@link ComponentScan},
 * then all you have to do is add below properties to configuration
 * (application.properties/yml) file.
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

  @Bean
  public HandshakeHandler uuidHanshakeHandler() {
    return new UUIDHandshakeHandler();
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
      .setHandshakeHandler(uuidHanshakeHandler());
  }

}