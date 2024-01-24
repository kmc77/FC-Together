package com.project.together.config.handler;

import com.project.together.domain.Member;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        // 사용자 인증 로직 구현
        // 예: 사용자 정보를 DB에서 조회하여 인증 처리

        // 비밀번호 일치 여부 검사
        if (!passwordEncoder.matches(password, getMemberPasswordFromDatabase(username))) {
            throw new BadCredentialsException("Invalid password");
        }

        return new UsernamePasswordAuthenticationToken(username, password);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    // 사용자 정보를 DB에서 조회하여 비밀번호를 가져오는 메서드
    private String getMemberPasswordFromDatabase(String username) {
        // DB에서 username에 해당하는 사용자 정보를 조회하여 비밀번호를 반환하는 로직을 구현해야 합니다.
        // 이 메서드를 사용자 정보 조회 로직에 맞게 수정해주세요.
        Member member = fetchMemberFromDatabase(username);
        if (member != null) {
            return member.getMember_pw();
        }
        return null;
    }

    // 사용자 정보를 DB에서 조회하는 메서드 (예시)
    private Member fetchMemberFromDatabase(String username) {
        // DB에서 username에 해당하는 사용자 정보를 조회하는 로직을 구현해야 합니다.
        // 이 메서드를 사용자 정보 조회 로직에 맞게 수정해주세요.
        // 예시로 Member 객체를 반환하도록 구현했습니다.
        Member member = new Member();
        member.setMember_name(username);
        member.setMember_pw("$2a$10$0Wx1rZ0xhFhKjkiMkRg6OeYf9S3G8rJlLjIL.7cUkH7FJW6z7Zu72"); // 예시: 암호화된 비밀번호
        return member;
    }
}
