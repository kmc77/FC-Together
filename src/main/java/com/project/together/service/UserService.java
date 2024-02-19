package com.project.together.service;

import com.project.together.domain.*;
import com.project.together.mapper.MyMapper;
import com.project.together.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final MyMapper myMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    public int idCheck(String username) {
        int count = userMapper.idCheck(username);
        return (count > 0) ? -1 : 1;
    }

    public int emailCheck(String userEmail) {
        int count = userMapper.emailCheck(userEmail);
        return (count > 0) ? -1 : 1;
    }


    public void joinUser(User user) {
        user.setUser_role("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userMapper.joinUser(user);
    }

    public List<K5_Player> getK5Players() {
        return userMapper.getK5Players();
    }

    public List<K7_Player> getK7Players() {
        return userMapper.getK7Players();
    }

    public List<S_Player> getSPlayers() {
        return userMapper.getSPlayers();
    }


    public User getFullUserInfo(String username) {
        return userMapper.findByUsername(username);
    }


    public void deleteUser(String username) {
        User user = userMapper.findByUsername(username);

        // Qna 테이블에서 관련 레코드를 먼저 삭제합니다.
        myMapper.deleteByUserId(user.getUsername());

        // User 테이블에서 해당 사용자를 삭제합니다.
        userMapper.delete(user);
    }
}
