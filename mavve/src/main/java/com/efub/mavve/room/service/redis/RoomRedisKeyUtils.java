package com.efub.mavve.room.service.redis;

public class RoomRedisKeyUtils {
    private static final String ROOM_PREFIX = "room:";
    private static final String SONG_LIST_SUFFIX = ":songs";
    private static final String CURRENT_SONG_SUFFIX = ":current";
    private static final String INSIDE_USERS_SUFFIX = ":users";

    public static String getSongListKey(Long roomCode) {
        return ROOM_PREFIX + roomCode + SONG_LIST_SUFFIX;
    }

    public static String getCurrentSongKey(Long roomCode) {
        return ROOM_PREFIX + roomCode + CURRENT_SONG_SUFFIX;
    }

    public static String getUserListKey(Long roomCode) {
        return ROOM_PREFIX + roomCode + INSIDE_USERS_SUFFIX;
    }
}
