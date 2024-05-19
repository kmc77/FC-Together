package com.project.together.mapper;

import com.project.together.domain.ClubVideo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MainMapper {
    List<ClubVideo> getAllClubVideos();
}
