package org.kittenboot.example;

import org.kittenboot.kittenserver.packets.GenericPacket;
import org.kittenboot.kittenserver.properties.RoomProperties;
import org.kittenboot.kittenserver.abstracted.AbstractLobbyService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Set;
import java.util.function.Consumer;

/**
 * In order to inject your own implementation of {@link AbstractLobbyService},
 * use <b>{@link Primary}</b> annotation.
 */
@Service
public class ExampleLobbyService extends AbstractLobbyService<ExamplePlayer, ExampleRoom> {

  @Override
  protected ExamplePlayer constructPlayer(Principal principal, Consumer<GenericPacket> sendPacket) {
    // free tip:
    // if you wan't to setup player's physical body, health or other things, ExampleRoom#init is a much better idea
    // this is the right place to construct player, and that's basically all.

    // The client hasn't even finished a handshake yet, so don't even try to send him any packet, it won't work.
    return new ExamplePlayer(principal, sendPacket);
  }

  @Override
  protected ExampleRoom constructRoom(Set<ExamplePlayer> players, RoomProperties config) {
    return new ExampleRoom(players, config);
  }

}
