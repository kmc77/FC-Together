package com.project.together.service;

import com.project.together.domain.K5_Player;
import com.project.together.domain.K7_Player;
import com.project.together.domain.Member;
import com.project.together.domain.S_Player;
import com.project.together.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MemberService {

    private final MemberMapper memberMapper;


    @Autowired
    public MemberService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }


    //회원 조회
    public List<Member> getAllMembers() {
        return memberMapper.getAllMembers();
    }

    //회원 정보 입력
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

    public boolean authenticateMember(String member_id, String member_pw) {
        String storedPassword = memberMapper.getPasswordById(member_id);
        return storedPassword != null && storedPassword.equals(member_pw);
    }

}