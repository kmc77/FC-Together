package com.project.together.mapper;

import com.project.together.domain.K5_Player;
import com.project.together.domain.K7_Player;
import com.project.together.domain.TeamStaff;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TeamMapper {
    TeamStaff getAllTeamStaff();

    List<K5_Player> getK5Player();

    List<K7_Player> getK7Player();
}