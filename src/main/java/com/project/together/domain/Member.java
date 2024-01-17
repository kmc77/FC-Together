package com.project.together.domain;

import lombok.Data;

@Data
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

    public String member_pw() {
        return member_pw;
    }


    public String getMember_id() {
        return member_id;
    }

    public String getMember_name() {
        return member_name;
    }

    public String getMember_pw() {
        return member_pw;
    }
}
