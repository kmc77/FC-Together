package com.project.together.service;

import com.project.together.config.jwt.JwtTokenProvider;
import com.project.together.domain.Member;
import com.project.together.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Service
public class MemberService {

    private final MemberMapper memberMapper;


    @Autowired
    public MemberService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    //회원 아이디 조회
    public Member getMemberById(String memberId) {
        Member member = memberMapper.getMemberById(memberId);

        if (member == null) {
            System.out.printf("사용자 아이디가 없습니다." + memberId);
        }
        return member;
    }

    //회원 조회
    public List<Member> getAllMembers() {
        return memberMapper.getAllMembers();
    }

    //회원 정보 입력
    public void insertMember(Member member) {
        memberMapper.insertMember(member);
    }

    //회원 정보 수정
    public void updateMember(Member member) {
        memberMapper.updateMember(member);
    }

    //회원 정보 삭제
    public void deleteMember(String memberId) {
        memberMapper.deleteMember(memberId);
    }


 /*   @Override
    public UserDetails loadUserByUsername(String memberId) throws UsernameNotFoundException {
        // 사용자 정보를 데이터베이스 등에서 조회하고 UserDetails 객체를 반환하는 로직을 구현해야 합니다.
        // 예시로 MemberMapper를 사용해서 사용자 정보를 조회하는 코드를 작성하겠습니다.
        Member member = memberMapper.getMemberById(memberId);

        if (member == null) {
            throw new UsernameNotFoundException("사용자 아이디가 없습니다: " + memberId);
        }

        // 사용자 정보를 기반으로 UserDetails 객체를 생성하여 반환합니다.
        return new User(member.getMemberId(), member.getPassword(), Collections.emptyList());
    }

    @Transactional
    public TokenInfo login(String memberId, String password) {
        // 1. Login ID/PW 를 기반으로 Authentication 객체 생성
        // 이때 authentication 는 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(memberId, password);

        // 2. 실제 검증 (사용자 비밀번호 체크)이 이루어지는 부분
        // authenticate 매서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드가 실행
        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication);

        return tokenInfo;
    }
*/

}
