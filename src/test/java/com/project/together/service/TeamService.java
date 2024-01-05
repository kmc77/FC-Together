package com.project.together.service;

import com.project.together.domain.TeamStaff;
import com.project.together.mapper.TeamMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamMapper teamMapper;

    public TeamStaff getAllTeamStaff() {
        return teamMapper.getAllTeamStaff();
    }
}
