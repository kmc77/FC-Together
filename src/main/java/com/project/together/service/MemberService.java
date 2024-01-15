package com.project.together.service;

import com.project.together.domain.K5_Player;
import com.project.together.domain.K7_Player;
import com.project.together.domain.Member;
import com.project.together.domain.S_Player;
import com.project.together.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        Member member = memberMapper.idCheck(memberId);
        return (member == null) ? -1 : 1;
    }

    //email 중복검사
    public int emailCheck(String memberEmail) {
        Member member = memberMapper.emailCheck(memberEmail);
        return (member == null) ? -1 : 1;
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
}
