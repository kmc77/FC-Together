package com.project.together.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.project.together.config.auth.PrincipalDetails;

import java.util.Date;

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

    public static String createPasswordResetToken(int userId) {
        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.PASSWORD_RESET_EXPIRATION_TIME))
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

    //토큰에서 사용자 이름을 추출
    public static String getUsername(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getSubject();
    }
}
