package com.project.together.config.jwt;

import lombok.Data;

public interface JwtProperties {
    String SECRET = "kcc5081"; // 서버만 알고 있는 비밀값
    int EXPIRATION_TIME = 60000 * 10; // 액세스 토큰 만료 시간: 10분
    int REFRESH_EXPIRATION_TIME = 60000 * 60 * 24; // 리프레시 토큰 만료 시간: 24시간
    int PASSWORD_RESET_EXPIRATION_TIME = 60000 * 15; // 비밀번호 재설정 토큰 만료 시간: 15분

    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
    String REFRESH_TOKEN_HEADER_STRING = "RefreshToken "; // 리프레시 토큰 헤더 키
}
