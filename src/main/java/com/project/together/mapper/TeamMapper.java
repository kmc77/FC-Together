package com.project.together.mapper;

import com.project.together.domain.TeamStaff;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TeamMapper {
    TeamStaff getAllTeamStaff();
}