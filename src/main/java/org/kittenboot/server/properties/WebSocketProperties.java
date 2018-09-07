package org.kittenboot.server.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ToString
@ConfigurationProperties("socket")
public class WebSocketProperties {

  private String registerEndpoint;

  private String allowedOrigins;

  private String messageBrokerPrefix;

  private String applicationDestinationPrefix;

}
