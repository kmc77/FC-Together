package com.project.together.service;

import com.project.together.domain.*;
import com.project.together.mapper.MemberMapper;
import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public interface MemberService {

    List<Member> getAllMembers();

    void joinMember(Member member);

    void updateMember(Member member);

    void deleteMember(String memberId);

    int idCheck(String memberId);

    int emailCheck(String memberEmail);

    List<K5_Player> getK5Players();

    List<K7_Player> getK7Players();

    List<S_Player> getSPlayers();


    Optional<Member> login(Member member);
}
