package com.project.together.domain;

import lombok.*;
import org.apache.ibatis.mapping.FetchType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member implements UserDetails {

    private String member_id;
    private String member_pw;
    private List<String> member_roles;
    private String member_name;
    private String member_gender;
    private String member_phone;
    private String member_email;
    private String member_birth;
    private String member_address;
    private String member_like_player;
    private String member_like_player_gb;
    private String member_marketing;




    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.member_roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return member_id;
    }

    @Override
    public String getPassword() {
        return member_pw;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    public String member_pw() {
        return member_pw;
    }


}
