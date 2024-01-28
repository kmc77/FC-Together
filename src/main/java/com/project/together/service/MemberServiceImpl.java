package com.project.together.service;

import com.project.together.domain.*;
import com.project.together.mapper.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberMapper memberMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberServiceImpl(MemberMapper memberMapper, PasswordEncoder passwordEncoder) {
        this.memberMapper = memberMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<Member> getAllMembers() {
        return memberMapper.getAllMembers();
    }

    @Override
    public void joinMember(Member member) {
        // 비밀번호 인코딩
        String encodedPassword = passwordEncoder.encode(member.getMember_pw());
        member.setMember_pw(encodedPassword);

        memberMapper.joinMember(member);
    }

    @Override
    public void updateMember(Member member) {
        memberMapper.updateMember(member);
    }

    @Override
    public void deleteMember(String memberId) {
        memberMapper.deleteMember(memberId);
    }

    @Override
    public int idCheck(String memberId) {
        int count = memberMapper.idCheck(memberId);
        return (count > 0) ? -1 : 1;
    }

    @Override
    public int emailCheck(String memberEmail) {
        int count = memberMapper.emailCheck(memberEmail);
        return (count > 0) ? -1 : 1;
    }

    @Override
    public List<K5_Player> getK5Players() {
        return memberMapper.getK5Players();
    }

    @Override
    public List<K7_Player> getK7Players() {
        return memberMapper.getK7Players();
    }

    @Override
    public List<S_Player> getSPlayers() {
        return memberMapper.getSPlayers();
    }

  /*  @Override
    public boolean authenticateMember(String member_id, String member_pw) {
        String storedPassword = memberMapper.getPasswordById(member_id);
        return storedPassword != null && storedPassword.equals(member_pw);
    }*/

    @Override
    public Optional<Member> login(Member member) {
        System.out.println("서비스 임플 member = " + member);
        return memberMapper.findByMemberId(String.valueOf(member));
    }
}

