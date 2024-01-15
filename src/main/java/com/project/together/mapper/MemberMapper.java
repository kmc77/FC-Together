package com.project.together.mapper;

import com.project.together.domain.K5_Player;
import com.project.together.domain.K7_Player;
import com.project.together.domain.Member;
import com.project.together.domain.S_Player;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {

    Member getMemberById(String memberId);

    Member idCheck(String memberId);

    Member emailCheck(String memberEmail);

    List<Member> getAllMembers();


    void updateMember(Member member);

    void deleteMember(String memberId);

    void joinMember(Member member);

    List<K5_Player> getK5Players();

    List<K7_Player> getK7Players();

    List<S_Player> getSPlayers();
}
