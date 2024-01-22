package com.project.together.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data

public class Member {

    private String member_id;
    private String member_pw;
    private String member_roles;
    private String member_name;
    private String member_gender;
    private String member_phone;
    private String member_email;
    private String member_birth;
    private String member_address;
    private String member_like_player;
    private String member_like_player_gb;
    private String member_marketing;


    public List<GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        Arrays.stream(this.member_roles.split(","))
                .map(SimpleGrantedAuthority::new)
                .forEach(authorities::add);
        return authorities;
    }
}
