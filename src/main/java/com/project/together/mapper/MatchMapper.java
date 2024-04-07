package com.project.together.mapper;

import com.project.together.domain.Match;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MatchMapper {
    List<Match> selectResult();

    List<Match> selectSchedule();
}
