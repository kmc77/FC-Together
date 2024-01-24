package com.project.together.service;

import com.project.together.domain.*;
import com.project.together.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MemberService {


    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public MemberService(MemberMapper memberMapper, PasswordEncoder passwordEncoder) {
        this.memberMapper = memberMapper;
        this.passwordEncoder = passwordEncoder;
    }


    //회원 조회
    public List<Member> getAllMembers() {
        return memberMapper.getAllMembers();
    }

    // 회원 정보 입력
 /*   public void joinMember(Member member) {
        // 비밀번호 인코딩
        String encodedPassword = passwordEncoder.encode(member.getMember_pw());
        member.setMember_pw(encodedPassword);

        memberMapper.joinMember(member);
    }*/

    // 회원 정보 입력
    public void joinMember(Member member) {
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
    public boolean authenticateMember(String member_id, String member_pw) {
        System.out.println("SSSmember_id = " + member_id + " SSSmember_pw = " + member_pw);
        String storedPassword = memberMapper.getPasswordById(member_id);
        System.out.println("SSSstoredPassword = " + storedPassword);
        return storedPassword != null && storedPassword.equals(member_pw);
    }


   /* //Jwt
    public Member findByMemberId(String login_id) {
        return memberMapper.findByMemberId(login_id);
    }*/


}
