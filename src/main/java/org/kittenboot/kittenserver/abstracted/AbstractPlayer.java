package org.kittenboot.kittenserver.abstracted;

import org.kittenboot.kittenserver.packets.GenericPacket;
import org.kittenboot.kittenserver.required.PlayerHolder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.security.Principal;
import java.util.Objects;
import java.util.function.Consumer;

import static org.kittenboot.kittenserver.packets.GenericPacket.buildPacket;

@ToString(exclude = "room")
public abstract class AbstractPlayer<R extends AbstractRoom> {

  /**
   * Player's Spring security principal.
   * Used for two things:
   * <ul>
   *   <li>
   *     To retrieve player from {@link PlayerHolder}
   *     using {@link PlayerHolder#getPlayer(Principal)}
   *   </li>
   *   <li>
   *     To send messages using {@link #send(String, Object)}
   *   </li>
   * </ul>
   */
  @Getter
  protected final Principal principal;

  private final Consumer<GenericPacket> sendPacket;

  @Getter
  @Setter
  protected R room;

  public AbstractPlayer(@NonNull Principal principal,
                        @NonNull Consumer<GenericPacket> sendPacket) {

    this.principal = principal;
    this.sendPacket = sendPacket;
  }

  /**
   * Sends data to player.<br>
   * @param destination data destination.<br>
   *                    Keep in mind two things:
   *                    <ul>
   *                      <li><b>Application property (${socket.message.broker.prefix}) + "/"</b> is prepended to the destination parameter</li>
   *                      <li>client-side must also prepend <b> "/user/"</b></li>
   *                    </ul>
   *                    So, assuming that WebSocketConfiguration.DESTINATION_PREFIX is "/game_get", and destination is "you_died", then
   *                    client-side must subscribe to <b>/user/game_get/you_died</b>.
   */
  public void send(String destination, Object data) {
    sendPacket.accept(
      GenericPacket.buildPacket(
        principal,
        destination,
        data
      )
    );
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    AbstractPlayer that = (AbstractPlayer) o;
    return Objects.equals(principal, that.principal);
  }

  @Override
  public int hashCode() {
    return Objects.hash(principal);
  }
}
