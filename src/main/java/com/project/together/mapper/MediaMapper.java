package com.project.together.mapper;

import com.project.together.domain.Notice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MediaMapper {
    List<Notice> findAll();
}
