package com.project.together.config.auth;

import com.project.together.domain.User;
import com.project.together.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


// 시큐리티 설정에서 loginProcessingUrl("/login");
// login 요청이 오면 자동으로 User DetailsService 타입으로 ioC되어 있는 loadUserByUsername 함수가 실행

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    // 시큐리티 session(내부 Authentication(내부 UserDetails))
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("PrincipalDetailsService : loadUserByUsername " + username);
        User userE = userMapper.findByUsername(username);
        System.out.println("PrincipalDetailsService memberE = " + userE);
        if (userE != null) {
            return new PrincipalDetails(userE);
        }
        throw new UsernameNotFoundException("입력하신 아이디를 찾을 수 없습니다.");
    }
}
