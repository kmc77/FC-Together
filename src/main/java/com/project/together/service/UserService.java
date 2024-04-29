package com.project.together.service;

import com.project.together.config.auth.PrincipalDetails;
import com.project.together.config.jwt.TokenUtils;
import com.project.together.domain.*;
import com.project.together.mapper.MyMapper;
import com.project.together.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        user.setRoles("ROLE_USER");
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

    public List<W1_Player> getW1Players() {
        return userMapper.getW1Players();
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


    public User findIDByPhoneNum(String phone) {
        return userMapper.findIDByPhoneNum(phone);
    }

    public User findIDByEmail(String email) {
        return userMapper.findIDByEmail(email);
    }

    public User findByUsernameAndEmail(String username, String email) {
        return userMapper.findByUsernameAndEmail(username, email);
    }

    public User getUserByResetPasswordToken(String token) {
        // 토큰에서 username 추출
        String username = TokenUtils.getUsername(token);

        System.out.println("서비스 ====== username = " + username);
        // 데이터베이스에서 username을 기반으로 사용자 검색
        return userMapper.findByUsername(username);
    }


    // 비밀번호 업데이트 메서드
    public void updatePassword(User user, String newPassword) {
        String encodedPassword = bCryptPasswordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
        userMapper.updateUserPassword(user);
    }

}
