package com.project.together.service.team;

import com.project.together.domain.teamstaff.TeamStaff;
import com.project.together.mapper.team.TeamMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamMapper teamMapper;

    public TeamStaff test() {
        return teamMapper.test();
    }
}
