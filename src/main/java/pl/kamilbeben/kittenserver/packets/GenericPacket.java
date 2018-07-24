package pl.kamilbeben.kittenserver.packets;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.security.Principal;

import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter(PROTECTED)
@NoArgsConstructor(access = PROTECTED)
public class GenericPacket<T> {

  private Principal principal;
  private String destination;
  private T data;

  public static <U> GenericPacket<U> buildPacket(String destination, U data) {
    return buildPacket(null, destination, data);
  }

  public static <U> GenericPacket<U> buildPacket(Principal principal, String destination, U data) {
    GenericPacket packet = new GenericPacket();

    packet.principal = principal;
    packet.destination = destination;
    packet.data = data;

    return packet;
  }



}

