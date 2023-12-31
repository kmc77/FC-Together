package com.project.together.service;

import com.project.together.domain.TeamStaff;
import com.project.together.mapper.TeamMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamService  {

    @Autowired
    private final TeamMapper teamMapper;

    public TeamStaff test() {
        return teamMapper.testt();
    }
}
