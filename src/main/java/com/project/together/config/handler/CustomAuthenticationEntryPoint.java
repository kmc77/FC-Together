package com.project.together.config.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        // 인증 실패 메시지 request에 추가
        request.getSession().setAttribute("authMessage", "접근 권한이 없습니다. 로그인 또는 회원가입 후 이용해 주세요.");

        // 로그인 페이지로 리다이렉트
        response.sendRedirect("/user/loginform");
    }
}
