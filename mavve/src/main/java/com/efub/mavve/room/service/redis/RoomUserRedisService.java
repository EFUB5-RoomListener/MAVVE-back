package com.efub.mavve.room.service.redis;

import com.efub.mavve.auth.domain.User;
import com.efub.mavve.global.exception.ExceptionCode;
import com.efub.mavve.global.exception.MavveException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomUserRedisService {
    private final StringRedisTemplate stringRedisTemplate;

    // 현재 방 사용자 추가
    public void addUser(Long roomCode, User user, String sessionId){
        try{
            String userListKey = RoomRedisKeyUtils.getUserListKey(roomCode);
            String userId = Long.toString(user.getUserId());
            stringRedisTemplate.opsForList().rightPush(userListKey, userId);

            String userRoomKey = RoomRedisKeyUtils.getUserRoomKey(sessionId);
            stringRedisTemplate.opsForValue().set(userRoomKey, roomCode.toString());
        } catch (Exception e){
            throw new MavveException(ExceptionCode.REDIS_SAVE_ERROR);
        }
    }

    // 현재 방에 남아있는 사용자가 있는지 확인
    public boolean hasUsers(Long roomCode){
        String key = RoomRedisKeyUtils.getUserListKey(roomCode);
        Long userCount = stringRedisTemplate.opsForList().size(key);

        return !(userCount == 0 || userCount == null);
    }

    // 현재 방에 남이있는 사용자 전체 조회
    public List<String> getAllUsers(Long roomCode) {
        String key = RoomRedisKeyUtils.getUserListKey(roomCode);
        List<String> userList = stringRedisTemplate.opsForList().range(key, 0, -1);

        if(userList == null || userList.isEmpty()) return Collections.emptyList();
        return userList;
    }

    // 방을 나간 경우 해당 사용자 리스트에서 제거
    public void deleteUser(User user, String sessionId) {
        try{
            String key = RoomRedisKeyUtils.getUserRoomKey(sessionId);
            Long roomCode = Long.parseLong(stringRedisTemplate.opsForValue().get(key));
            stringRedisTemplate.delete(key);

            List<String> usersInRoom = getAllUsers(roomCode);
            String userListkey = RoomRedisKeyUtils.getUserListKey(roomCode);
            String deleteUserId = Long.toString(user.getUserId());

            for(String userId: usersInRoom){
                if(userId.equals(deleteUserId)){
                    stringRedisTemplate.opsForList().remove(userListkey, 1, userId);
                    break;
                }
            }
        } catch (Exception e){
            throw new MavveException(ExceptionCode.REDIS_DELETE_ERROR);
        }
    }

    // 방 구독했던 사용자인지 확인
    public boolean IfUserSubscribed(String sessionId) {
        String key = RoomRedisKeyUtils.getUserRoomKey(sessionId);
        return Boolean.TRUE.equals(stringRedisTemplate.hasKey(key));
    }

}
