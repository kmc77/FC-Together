package com.project.together.mapper;

import com.project.together.domain.K5_Player;
import com.project.together.domain.K7_Player;
import com.project.together.domain.TeamStaff;
import com.project.together.domain.W1_Player;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TeamMapper {

    List<K5_Player> getK5Players();

    List<K7_Player> getK7Players();

    List<W1_Player> getW1Players();

    List<TeamStaff> getTeamStaff();
}