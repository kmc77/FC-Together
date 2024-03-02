package com.project.together.mapper;

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
}
