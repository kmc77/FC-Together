package com.project.together.mapper;

import com.project.together.domain.News;
import com.project.together.domain.Notice;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MediaMapper {
    List<Notice> findAll(Map<String, Integer> params);

    Notice findNoticeByNoticeNum(int noticeNum);

    List<News> getNewsList(Map<String, Integer> params);
}
