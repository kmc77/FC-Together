package com.project.together.service;

import com.project.together.domain.Member;
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

    public Member getMemberByUsername(String username) {
        Member member = memberMapper.getMemberByUsername(username);

        if (member == null) {
            System.out.printf("사용자명이 없습니다." + username);
        }
        return member;
    }

}
