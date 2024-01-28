package com.project.together.config.handler;

import com.project.together.domain.MemberDetails;
import com.project.together.service.MemberDetailsService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;

/**
 * 전달받은 사용자의 아이디와 비밀번호를 기반으로 비즈니스 로직을 처리하여 사용자의 ‘인증’에 대해서 검증을 수행하는 클래스입니다.
 * CustomAuthenticationFilter로 부터 생성한 토큰을 통하여 ‘MemberDetailsService’를 통해 데이터베이스 내에서 정보를 조회합니다.
 */
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    @Resource
    private MemberDetailsService memberDetailsService;
    @NonNull
    private BCryptPasswordEncoder passwordEncoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.debug("2.CustomAuthenticationProvider");
        System.out.println("프로바이더 authentication = " + authentication);
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        System.out.println("검증 token = " + token);

        // 'AuthenticaionFilter' 에서 생성된 토큰으로부터 아이디와 비밀번호를 조회함
        String userId = token.getName();
        String userPw = (String) token.getCredentials();
        System.out.println("시도 userId = " + userId);
        System.out.println("시도 userPw = " + userPw);


        // Spring Security - UserDetailsService를 통해 DB에서 아이디로 사용자 조회
        MemberDetails memberDetails = (MemberDetails) memberDetailsService.loadUserByUsername(userId);
        System.out.println("DB 사용자 조회 memberDetails = " + memberDetails);
        if (!(memberDetails.getMember_pw().equalsIgnoreCase(userPw) && memberDetails.getMember_pw().equalsIgnoreCase(userPw))) {
            throw new BadCredentialsException(memberDetails.getMember_name() + "Invalid password");
        }
        return new UsernamePasswordAuthenticationToken(memberDetails, userPw, memberDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

}
