package com.project.together.domain;

import com.nimbusds.oauth2.sdk.TokenIntrospectionSuccessResponse;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@RequiredArgsConstructor
public class User {

    private int id;
    private String username;
    private String password;
    private String email;
    private String user_role;
    private String user_real_name;
    private String user_gender;
    private String user_phone;
    private String user_birth;
    private String user_address;
    private String user_like_player;
    private String user_like_player_gb;
    private String user_marketing;

    // OAuth2 를 위해 구성한 추가 필드 2개
    private String provider;   //소셜 플랫폼 이름
    private String providerId; //소셜 계정 고유값(id)

    @Builder
    public User(String username, String password, String email, String user_role, String provider, String providerId, String user_real_name) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.user_role = user_role;
        this.provider = provider;
        this.providerId = providerId;
        this.user_real_name = user_real_name;
    }

    // ENUM으로 안하고 ,로 해서 구분해서 ROLE을 입력 -> 그걸 파싱!!
    public List<String> getRoleList() {
        if (this.user_role.length() > 0) {
            return Arrays.asList(this.user_role.split(","));
        }
        return new ArrayList<>();
    }
}
