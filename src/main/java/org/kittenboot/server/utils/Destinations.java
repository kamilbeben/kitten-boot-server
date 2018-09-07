package org.kittenboot.server.utils;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class Destinations {

  // mappings
  public static final String JOIN_PUBLIC_QUEUE = "/join_public_queue";
  public static final String START_PRIVATE_ROOM = "/start_private_room";
  public static final String JOIN_PRIVATE_ROOM= "/join_private_room";

  // responses
  public static final String ROOM_UPDATE = "/room_update";
  public static final String PLAYER_JOINED_ROOM = "/player_joined_room";
  public static final String PLAYER_LEFT_ROOM = "/player_left_room";

  public static final String QUEUE_NOT_FOUND = "/queue_not_found";
  public static final String QUEUE_CREATED = "/queue_created";
  public static final String JOINED_QUEUE = "/joined_queue";
  public static final String PLAYER_JOINED_QUEUE = "/player_joined_queue";
  public static final String PLAYER_LEFT_QUEUE = "/player_left_queue";

}
