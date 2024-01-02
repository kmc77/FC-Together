package com.project.together.service;

import com.project.together.domain.Member;
import com.project.together.mapper.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;

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
}
