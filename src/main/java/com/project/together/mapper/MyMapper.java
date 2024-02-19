package com.project.together.mapper;

import com.project.together.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;


@Mapper
public interface MyMapper {

    void saveQna(Qna qna);

    User findByUsername(String username);

    List<Qna> findQnaByUsername(String username);

    Qna findQnaByQnaNum(int qnaNum);

    void deleteQna(@Param("username") String username, @Param("qnaNum") int qnaNum);

    void deleteByUserId(String username);
}
