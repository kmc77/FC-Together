package com.project.together.service;

import com.project.together.config.jwt.JwtTokenProvider;
import com.project.together.domain.*;
import com.project.together.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = false)

public class MemberService {

    private final MemberMapper memberMapper;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;


    public TokenInfo login(String member_id, String member_pw) {
        try {
            // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
            // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member_id, member_pw);
            System.out.println("Login ID/PW 를 기반 authenticationToken = " + authenticationToken);

            // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
            // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            System.out.println("실제 검증 authentication = " + authentication);

            // 3. 인증 정보를 기반으로 JWT 토큰 생성
            TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);
            return tokenInfo;
        } catch (AuthenticationException e) {
            // 인증 실패 처리
            System.out.println("인증 실패: " + e.getMessage());
            // 예외를 처리하거나 예외를 다시 던지거나, 실패 시 반환할 값을 결정합니다.
            return null;
        }
    }


    @Autowired
    public MemberService(MemberMapper memberMapper, AuthenticationManagerBuilder authenticationManagerBuilder, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.memberMapper = memberMapper;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }


    //회원 조회
    public List<Member> getAllMembers() {
        return memberMapper.getAllMembers();
    }

    // 회원 정보 입력
    public void joinMember(Member member) {
        // 비밀번호 인코딩
        String encodedPassword = passwordEncoder.encode(member.getMember_pw());
        member.setMember_pw(encodedPassword);

        memberMapper.joinMember(member);
    }

    //회원 정보 수정
    public void updateMember(Member member) {
        memberMapper.updateMember(member);
    }

    //회원 정보 삭제
    public void deleteMember(String memberId) {
        memberMapper.deleteMember(memberId);
    }


    //ID 중복검사
    public int idCheck(String memberId) {
        int count = memberMapper.idCheck(memberId);
        return (count > 0) ? -1 : 1;
    }

    //email 중복검사
    public int emailCheck(String memberEmail) {
        int count = memberMapper.emailCheck(memberEmail);
        return (count > 0) ? -1 : 1;
    }

    public List<K5_Player> getK5Players() {
        return memberMapper.getK5Players();
    }

    public List<K7_Player> getK7Players() {
        return memberMapper.getK7Players();
    }

    public List<S_Player> getSPlayers() {
        return memberMapper.getSPlayers();
    }

    //로그인 회원인증
   /* public boolean authenticateMember(String member_id, String member_pw) {
        System.out.println("SSSmember_id = " + member_id + " SSSmember_pw = " + member_pw);
        String storedPassword = memberMapper.getPasswordById(member_id);
        System.out.println("SSSstoredPassword = " + storedPassword);
        return storedPassword != null && storedPassword.equals(member_pw);
    }*/

    //Jwt
    public Optional<Member> findByMemberId(String member_id) {
        return memberMapper.findByMemberId(member_id);
    }


}
