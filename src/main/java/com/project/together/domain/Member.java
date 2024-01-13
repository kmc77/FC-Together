package com.project.together.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member {
    private String memberId;
    private String memberPw;
    private String memberName;
    private String memberGender;
    private String memberPhone;
    private String memberEmail;
    private String memberBirth;
    private String memberAddress;
    private String memberLikePlayer;
    private String memberLikePlayerGb;
    private char memberMarketing;

    public String getPassword() {
        return memberPw;
    }

    public String getMemberId() {
        return memberId;
    }
}
