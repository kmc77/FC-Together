package com.project.together.domain;

import com.nimbusds.oauth2.sdk.TokenIntrospectionSuccessResponse;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Data
@RequiredArgsConstructor
public class User {

    private int id;
    private String username;
    private String password;
    private String email;
    private String roles;
    private String userRealName;
    private String userGender;
    private String userPhone;
    private String userBirth;
    private String userAddress;
    private String userLikePlayer;
    private String userLikePlayerGb;
    private String userMarketing;

    // OAuth2 를 위해 구성한 추가 필드 2개
    private String provider;   //소셜 플랫폼 이름
    private String providerId; //소셜 계정 고유값(id)

    @Builder
    public User(int id, String username, String password, String email, String roles, String provider, String providerId, String user_real_name, String user_phone, String user_birth, String user_address) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
        this.provider = provider;
        this.providerId = providerId;
        this.userRealName = user_real_name;
        this.userPhone = user_phone;
        this.userBirth = user_birth;
        this.userAddress = user_address;
    }

    // ENUM으로 안하고 ,로 해서 구분해서 ROLE을 입력 -> 그걸 파싱!!
  /*  public List<String> getRoleList() {
        if (this.user_role != null && !this.user_role.isEmpty()) {
            return Collections.singletonList(this.user_role);  // 단일 권한을 리스트로 변환
        }
        return new ArrayList<>();
    }*/

}
