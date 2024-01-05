package com.project.together.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    public static String KEY_TO_INCLUDE_IN_HTTP_HEADER = "Authorization";
    private String secretKey = "myJWTKey";
    private long expirationTime = 60 * 60 * 1000L;  // 토큰 만료 시간: 60분

    private final UserDetailsService userService;

    public JwtTokenProvider(UserDetailsService userService) {
        this.userService = userService;
    }

    // 객체 초기화, secretKey를 Base64로 인코딩
    @PostConstruct
    protected void secretKeyEncoding() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT 토큰 생성
    public String tokenIssuance(String userPk, List<String> roles) {
        Claims claims = Jwts.claims().setSubject(userPk); // JWT 페이로드에 저장되는 정보 단위, 일반적으로 사용자를 식별하는 값을 포함
        claims.put("roles", roles); // 키/값 쌍으로 정보를 저장
        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims) // 정보 저장
                .setIssuedAt(now) // 토큰 발급 시간 설정
                .setExpiration(new Date(now.getTime() + expirationTime)) // 만료 시간 설정
                .signWith(SignatureAlgorithm.HS256, secretKey)  // 서명에 사용될 암호화 알고리즘과 비밀 값 설정
                .compact();
    }

    // JWT 토큰에서 인증 정보 조회
    public Authentication permissionInquiry(String token) {
        UserDetails userDetails = userService.loadUserByUsername(getUserInfo(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    // 토큰 유효성 검사 및 만료일 확인
    public boolean validateToken(String jwtToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            System.out.println(claims);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 토큰에서 사용자 정보 추출
    String getUserInfo(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
    }
}
