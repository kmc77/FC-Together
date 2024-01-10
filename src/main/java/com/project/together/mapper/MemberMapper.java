package com.project.together.mapper;

import com.project.together.domain.Member;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {

    Member getMemberById(String memberId);

    List<Member> getAllMembers();


    void updateMember(Member member);

    void deleteMember(String memberId);

    void joinMember(Member member);

}
