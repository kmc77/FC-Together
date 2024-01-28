package com.project.together.service;

import com.project.together.domain.Member;
import com.project.together.domain.MemberDetails;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class MemberDetailsService implements UserDetailsService {
    private final MemberService memberService;
    private final MemberServiceImpl memberServiceImpl;

    public MemberDetailsService(MemberService memberService, MemberServiceImpl memberServiceImpl) {
        this.memberService = memberService;
        this.memberServiceImpl = memberServiceImpl;
    }

    @Override
    public UserDetails loadUserByUsername(String member_id) {
        Member member = Member
                .memberBuilder()
                .member_id(member_id)
                .build();
        System.out.println("디테일 서비스 member = " + member);

        // 사용자 정보가 존재하지 않는 경우
        if (member_id == null || member_id.isEmpty()) {
            return memberServiceImpl.login(member)
                    .map(m -> new MemberDetails(m, Collections.singleton(new SimpleGrantedAuthority(m.getMember_id()))))
                    .orElseThrow(() -> new AuthenticationServiceException(member_id));
        }
        // 비밀번호가 맞지 않는 경우
        else {
            System.out.println("디테일 서비스 member = " + member);
            return memberServiceImpl.login(member)
                    .map(m -> new MemberDetails(m, Collections.singleton(new SimpleGrantedAuthority(m.getMember_id()))))
                    .orElseThrow(() -> new BadCredentialsException(member_id));
        }
    }
}
