package com.project.together.config.handler;

import com.project.together.config.auth.ErrorCode;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthFailureHandler implements AuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        // 로그인 실패 시 처리할 로직 구현
        // 예: 에러 메시지 표시, 로그 기록 등

        // 예시: 로그인 실패 시 에러 메시지 출력
        String errorMessage = "로그인에 실패하였습니다.";
        request.setAttribute("errorMessage", errorMessage);
        request.getRequestDispatcher("/login").forward(request, response);
    }
}