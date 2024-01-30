package com.project.together.service;

import com.project.together.domain.*;
import com.project.together.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private UserMapper userMapper;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserMapper userMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userMapper = userMapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    public void joinMember(User user) {
        user.setUser_role("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userMapper.joinUser(user);
    }

    public int idCheck(String username) {
        int count = userMapper.idCheck(username);
        return (count > 0) ? -1 : 1;
    }

    public int emailCheck(String memberEmail) {
        int count = userMapper.emailCheck(memberEmail);
        return (count > 0) ? -1 : 1;
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


    public void joinUser(User user) {
        user.setUser_role("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userMapper.joinUser(user);
    }
}
