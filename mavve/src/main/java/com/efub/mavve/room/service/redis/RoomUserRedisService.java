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
    public void addUser(Long roomCode, User user){
        try{
            String key = RoomRedisKeyUtils.getUserListKey(roomCode);
            String userId = Long.toString(user.getUserId());
            stringRedisTemplate.opsForList().rightPush(key, userId);
        } catch (Exception e){
            throw new MavveException(ExceptionCode.REDIS_SAVE_ERROR);
        }
    }

    // 현재 방에 남아있는 사용자가 있는지 확인
    public boolean hasUsers(Long roomCode){
        String key = RoomRedisKeyUtils.getUserListKey(roomCode);
        List<String> userList = stringRedisTemplate.opsForList().range(key, 0, -1);

        return userList != null && !userList.isEmpty();
    }

    // 현재 방에 남이있는 사용자 전체 조회
    private List<String> getAllUsers(Long roomCode) {
        String key = RoomRedisKeyUtils.getUserListKey(roomCode);
        List<String> userList = stringRedisTemplate.opsForList().range(key, 0, -1);

        if(userList == null) return Collections.emptyList();
        return userList;
    }

    // 방을 나간 경우 해당 사용자 리스트에서 제거
    public void deleteUser(Long roomCode, User user) {
        try{
            List<String> usersInRoom = getAllUsers(roomCode);
            String key = RoomRedisKeyUtils.getUserListKey(roomCode);
            String deleteUserId = Long.toString(user.getUserId());

            for(String userId: usersInRoom){
                if(userId.equals(deleteUserId)){
                    stringRedisTemplate.opsForList().remove(key, 1, userId);
                }
            }
        } catch (Exception e){
            throw new MavveException(ExceptionCode.REDIS_DELETE_ERROR);
        }
    }
}
