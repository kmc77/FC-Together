package com.project.together.config;

import com.project.together.domain.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class SecurityUser implements UserDetails {

    private static final Long serialVersionUID = 1L;
    private final Member member;

    public SecurityUser(Member member) {
        this.member = member;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new LinkedList<GrantedAuthority>();
        list.add(new SimpleGrantedAuthority("auth1"));
        list.add(new SimpleGrantedAuthority("auth2"));
        return list;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getMemberId();
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
}


