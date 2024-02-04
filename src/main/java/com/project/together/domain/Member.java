package com.project.together.domain;

import lombok.*;

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
    private List<String> member_roles; // 추가된 roles 필드

    @Builder(builderMethodName = "memberBuilder", toBuilder = true)
    private Member(String member_id, String member_pw, String member_name
    ) {
        this.member_id = member_id;
        this.member_pw = member_pw;
        this.member_name = member_name;
    }


}
