package com.project.together.mapper;

import com.project.together.domain.Notice;
import com.project.together.domain.Qna;
import com.project.together.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminMapper {

    List<User> getAllUsers();

    List<Qna> getAllQnAs();

    Qna findById(@Param("qnaNum") int qnaNum);

    int updateQnA(@Param("qnaNum") String qnaNum, @Param("authId") String authId, @Param("qnaReply") String qnaReply);

    void deleteQna(List<Integer> qnaNums);

    /* ======================================= */

    List<Notice> getAllNotice();

    Notice findNoticesById(@Param("noticeNum") int noticeNum);

    void insertNotice(Notice notice);

    void updateNotice(Notice notice);

    void deleteNotice(List<Integer> noticeNums);
}
