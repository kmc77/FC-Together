package com.project.together.config.oauth;


import com.project.together.config.auth.PrincipalDetails;
import com.project.together.config.jwt.JwtAuthenticationFilter;
import com.project.together.config.jwt.JwtProperties;
import com.project.together.config.jwt.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("OAuth2LoginSuccessHandler 입장 request = " + request);
        System.out.println("================ 소셜 로그인 후 토큰 발급 로직 필요함 ================");

        // 인증 객체에서 PrincipalDetails를 가져옵니다.
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        // 토큰을 생성합니다.
        String jwtToken = TokenUtils.createJwtToken(principalDetails);
        String refreshToken = TokenUtils.createRefreshToken(principalDetails);

        // 응답에 토큰을 추가합니다.
        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        // 토큰 정보를 확인할 수 있는 페이지로 리다이렉트
        response.sendRedirect("/");
    }

}

