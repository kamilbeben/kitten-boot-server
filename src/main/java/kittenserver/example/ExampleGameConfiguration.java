package kittenserver.example;


import kittenserver.required.AbstractGameConfiguration;
import kittenserver.required.AbstractLobbyService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@Configuration
@EnableWebSocketMessageBroker
public class ExampleGameConfiguration extends AbstractGameConfiguration {

  @Bean
  public AbstractLobbyService lobbyService() {
    return new ExampleLobbyService();
  }
}
