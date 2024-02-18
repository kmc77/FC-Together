package com.project.together.mapper;

import com.project.together.domain.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface MyMapper {

    void saveQna(Qna qna);

    User findByUsername(String username);

    List<Qna> findQnaByUsername(String username);
}
