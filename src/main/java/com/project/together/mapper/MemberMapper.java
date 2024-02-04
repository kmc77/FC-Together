package com.project.together.mapper;

import com.project.together.domain.K5_Player;
import com.project.together.domain.K7_Player;
import com.project.together.domain.Member;
import com.project.together.domain.S_Player;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MemberMapper {


    int idCheck(String memberId);

    int emailCheck(String memberEmail);

    List<Member> getAllMembers();


    void updateMember(Member member);

    void deleteMember(String memberId);

    void joinMember(Member member);

    List<K5_Player> getK5Players();

    List<K7_Player> getK7Players();

    List<S_Player> getSPlayers();

    String getPasswordById(String member_id);


}