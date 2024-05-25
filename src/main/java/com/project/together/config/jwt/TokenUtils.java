package com.project.together.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.project.together.config.auth.PrincipalDetails;

import java.util.Date;
import java.util.Random;

public class TokenUtils {
    public static String createJwtToken(PrincipalDetails principalDetails) {
        return JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .withClaim("roles", principalDetails.getUser().getRoles())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
    }

    public static String createRefreshToken(PrincipalDetails principalDetails) {
        return JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.REFRESH_EXPIRATION_TIME))
                .withClaim("id", principalDetails.getUser().getId())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
    }


    // 인증번호 토큰 생성
    public static String createVerificationToken(String username, String email) {
        int verificationCode = new Random().nextInt(999999); // 6자리 인증번호 생성
        return JWT.create()
                .withSubject(username) // JWT의 주제(subject)를 username으로 설정
                .withClaim("email", email) // 이메일은 별도의 클레임으로 저장
                .withClaim("code", verificationCode) // 인증번호 포함
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.VERIFICATION_TOKEN_EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
    }



    //토큰 유효성 검사
    public static boolean validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(JwtProperties.SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰 검증 메서드
    public static boolean verifyToken(String verificationToken, int userInputVerificationCode) {
        try {
            int tokenVerificationCode = extractVerificationCode(verificationToken); // 토큰에서 인증번호 추출
            return tokenVerificationCode == userInputVerificationCode; // 토큰의 인증번호와 사용자 입력 인증번호 비교
        } catch (Exception e) {
            // 토큰 검증 실패 (예: 토큰 만료, 잘못된 토큰 등)
            return false;
        }
    }


    //토큰에서 사용자 이름을 추출
    public static String getUsername(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getSubject();
    }

    // 토큰에서 인증번호를 추출
    public static int extractVerificationCode(String token) {

        try {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC512(JwtProperties.SECRET)).build().verify(token);
            return jwt.getClaim("code").asInt();
        } catch (JWTVerificationException exception){
            //Invalid signature/claims
            throw new RuntimeException("Invalid token");
        }
    }

    // 토큰 디코드 메서드
    /*public static DecodedJWT decodeToken(String jwtToken) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(JwtProperties.SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            return verifier.verify(jwtToken);
        } catch (JWTVerificationException exception) {
            // 토큰 검증 실패 시 예외 처리
            throw new RuntimeException("Invalid token", exception);
        }
    }*/
}
