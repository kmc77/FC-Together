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

    public List<Match> selectResult() {

        return matchMapper.selectResult();
    }

    public List<Match> selectSchedule() {

        return matchMapper.selectSchedule();
    }
}