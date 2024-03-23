package com.project.together.service;

import com.project.together.domain.K5_Player;
import com.project.together.domain.K7_Player;
import com.project.together.domain.TeamStaff;
import com.project.together.domain.W1_Player;
import com.project.together.mapper.TeamMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamMapper teamMapper;


    public List<K5_Player> getK5Player() {
        return teamMapper.getK5Player();
    }

    public List<K7_Player> getK7Player() {
        return teamMapper.getK7Player();
    }

    public List<W1_Player> getW1Player() {
        return teamMapper.getW1Player();
    }

    public List<TeamStaff> getTeamStaff() {
        return teamMapper.getTeamStaff();
    }

}
