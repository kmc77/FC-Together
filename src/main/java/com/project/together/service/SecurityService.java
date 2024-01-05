package com.project.together.service;

import com.project.together.config.SecurityUser;
import com.project.together.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityService implements UserDetailsService {

    private final MemberService memberService;

    @Autowired
    public SecurityService(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberService.getMemberByUsername(username);
        if (member == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }

        SecurityUser user = new SecurityUser(member);
        UserDetails userDetails = User.withUsername(username)
                .password(user.getPassword())
                .authorities(user.getAuthorities())
                .build();

        return userDetails;
    }
}
