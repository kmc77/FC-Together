package com.project.together.mapper;

import com.project.together.domain.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {



    int idCheck(String memberId);

    int emailCheck(String memberEmail);

    static List<Member> getAllMembers() {
        return null;
    }


    void updateMember(Member member);

    void deleteMember(String memberId);

/*    void joinMember(Member member);*/

    List<K5_Player> getK5Players();

    List<K7_Player> getK7Players();

    List<S_Player> getSPlayers();


    Member findByUsername(String username);


    void save(Member member);
}
