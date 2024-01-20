package com.project.together.service;

import com.project.together.domain.Member;
import com.project.together.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> memberOptional = memberMapper.findByMemberId(username);
        Member member = memberOptional.orElseThrow(() -> new UsernameNotFoundException("해당하는 유저를 찾을 수 없습니다."));

        return createUserDetails(member);
    }

    private UserDetails createUserDetails(Member member) {
        // 회원 가입 시 저장된 인코딩된 비밀번호와 비교하기 위해 passwordEncoder.matches() 메서드를 사용합니다.
        boolean passwordMatches = passwordEncoder.matches(member.getMember_pw(), member.getMember_pw());

        if (!passwordMatches) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        return User.builder()
                .username(member.getMember_id())
                .password(member.getMember_pw())
                .roles(member.getMember_roles().toArray(new String[0]))
                .build();
    }
}