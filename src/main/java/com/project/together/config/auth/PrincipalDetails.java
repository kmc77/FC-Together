package com.project.together.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.project.together.domain.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

/*     시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킨다.
       로그인을 진행이 완료가 되면 시큐리티 session을 만들어줍니다. (Security ContextHolder)
       오브젝트 타입 => Authentication 타입 객체
       Authentication 안에 User정보가 있어야 됨.
       User오브젝트타입=> UserDetails 타입 객체
       Security Session => Authentication => UserDetails(PrincipalDetails)*/
@Data
public class PrincipalDetails implements UserDetails, OAuth2User {
    private User user;
    private Map<String, Object> attributes;
    private String token;


    //Normal Login
    public PrincipalDetails(User user) {
        this.user = user;
    }

    // OAuth2 Login
    public PrincipalDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }


    //해당 유저의 권한을 리턴하는 로직
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRoles();
            }
        });
        return collect;
    }


    @Override
    public String getPassword() {
        return user.getPassword();
    }


    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 토큰 관련 getter, setter 추가
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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


    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }

    public User getUser() {
        return user;
    }

    public int getId() {
        return user.getId();
    }

    public String getUserRealName() {
        return user.getUserRealName();
    }

}
