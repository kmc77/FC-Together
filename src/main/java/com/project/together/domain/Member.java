package com.project.together.domain;

import lombok.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    private String member_id;
    private String member_pw;
    private String member_name;
    private String member_gender;
    private String member_phone;
    private String member_email;
    private String member_birth;
    private String member_address;
    private String member_like_player;
    private String member_like_player_gb;
    private String member_marketing;
    private String member_roles;  // 추가된 roles 필드


    // ENUM으로 안하고 ,로 해서 구분해서 ROLE을 입력 -> 그걸 파싱!!
    public List<String> getRoleList() {
        if (this.member_roles.length() > 0) {
            return Arrays.asList(this.member_roles.split(","));
        }
        return new ArrayList<>();
    }


}
