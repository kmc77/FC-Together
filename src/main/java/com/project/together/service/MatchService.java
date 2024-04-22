package com.project.together.service;

import com.project.together.domain.Match;
import com.project.together.mapper.MatchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final MatchMapper matchMapper;

    public List<Match> K5selectResult() {

        return matchMapper.K5selectResult();
    }

    public List<Match> K5selectSchedule() {

        return matchMapper.K5selectSchedule();
    }

    public List<Match> K7selectResult() {

        return matchMapper.K7selectResult();
    }

    public List<Match> K7selectSchedule() {

        return matchMapper.K7selectSchedule();
    }
}