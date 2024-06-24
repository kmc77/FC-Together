package com.project.together.service;

import com.project.together.domain.Match;
import com.project.together.domain.Team;
import com.project.together.mapper.MatchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchMapper matchMapper;


    @Cacheable(value = "clubVideosCache", key = "'K5selectSchedule'")
    public List<Match> K5selectSchedule() {
        return matchMapper.K5selectSchedule();
    }

    @Cacheable(value = "clubVideosCache", key = "'K5selectResult'")
    public List<Match> K5selectResult() {
        return matchMapper.K5selectResult();
    }

    @Cacheable(value = "clubVideosCache", key = "'K5leagueGb'")
    public List<Team> K5leagueGb() {
        return matchMapper.K5leagueGb();
    }

    @Cacheable(value = "clubVideosCache", key = "'K7selectSchedule'")
    public List<Match> K7selectSchedule() {
        return matchMapper.K7selectSchedule();
    }

    @Cacheable(value = "clubVideosCache", key = "'K7selectResult'")
    public List<Match> K7selectResult() {
        return matchMapper.K7selectResult();
    }

    @Cacheable(value = "clubVideosCache", key = "'K7leagueGb'")
    public List<Team> K7leagueGb() {
        return matchMapper.K7leagueGb();
    }

    @Cacheable(value = "clubVideosCache", key = "'W1selectSchedule'")
    public List<Match> W1selectSchedule() {
        return matchMapper.W1selectSchedule();
    }

    @Cacheable(value = "clubVideosCache", key = "'W1selectResult'")
    public List<Match> W1selectResult() {
        return matchMapper.W1selectResult();
    }

    @Cacheable(value = "clubVideosCache", key = "'W1leagueGb'")
    public List<Team> W1leagueGb() {
        return matchMapper.W1leagueGb();
    }
}