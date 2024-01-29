package com.project.together.service;

import com.project.together.domain.*;
import com.project.together.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemberService {

    private final MemberMapper memberMapper;

    @Autowired
    public MemberService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }

    public List<Member> getAllMembers() {
        return MemberMapper.getAllMembers();
    }

/*    public void joinMember(Member member) {
        memberMapper.joinMember(member);
    }*/

    public void updateMember(Member member) {
        memberMapper.updateMember(member);
    }

    public void deleteMember(String memberId) {
        memberMapper.deleteMember(memberId);
    }

    public int idCheck(String memberId) {
        int count = memberMapper.idCheck(memberId);
        return (count > 0) ? -1 : 1;
    }

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

    public Member findByUsername(String username) {
        return memberMapper.findByUsername(username);
    }


    public void save(Member member) {
        memberMapper.save(member);
    }


}
