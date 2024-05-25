package com.project.together.mapper;

import com.project.together.domain.Match;
import com.project.together.domain.Team;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MatchMapper {
    List<Match> K5selectSchedule();

    List<Match> K5selectResult();

    List<Team> K5leagueGb();

    List<Match> K7selectSchedule();

    List<Match> K7selectResult();

    List<Match> W1selectSchedule();

    List<Match> W1selectResult();
}