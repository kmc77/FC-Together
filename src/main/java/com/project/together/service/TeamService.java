package com.project.together.service;

import com.project.together.domain.K5_Player;
import com.project.together.domain.K7_Player;
import com.project.together.domain.TeamStaff;
import com.project.together.domain.W1_Player;
import com.project.together.mapper.TeamMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamMapper teamMapper;


    public List<K5_Player> getK5Players() {
        return teamMapper.getK5Players();
    }

    public List<K7_Player> getK7Players() {
        return teamMapper.getK7Players();
    }

    public List<W1_Player> getW1Players() {
        return teamMapper.getW1Players();
    }

    public List<TeamStaff> getTeamStaff() {
        return teamMapper.getTeamStaff();
    }

}
