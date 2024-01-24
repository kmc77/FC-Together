package com.project.together.config.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // 로그인 성공 시 처리할 로직 구현
        // 예: 사용자를 이동시키거나 특정 작업을 수행

        // 예시: 로그인 성공 시 홈페이지로 이동
        response.sendRedirect("/home");
    }
}

