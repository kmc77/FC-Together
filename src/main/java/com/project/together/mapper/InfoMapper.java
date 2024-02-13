package com.project.together.mapper;

import com.project.together.domain.*;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface InfoMapper {

    void saveQna(Qna qna);
}
