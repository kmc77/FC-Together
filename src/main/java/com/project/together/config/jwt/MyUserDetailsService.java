package com.project.together.config.jwt;

import com.project.together.domain.Member;
import com.project.together.mapper.MemberMapper;
import com.project.together.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private MemberMapper memberMapper;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("디테일 서비스 username = " + username);
        Member member = memberMapper.findByUsername(username);
        System.out.println("디테일 서비스 member = " + member);
        if (member == null) {
            System.out.println("디테일 서비스 if 문 member = " + member);
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new User(member.getMember_id(), member.getMember_pw(), new ArrayList<>());
    }
}
