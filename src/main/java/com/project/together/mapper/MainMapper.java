package com.project.together.mapper;

import com.project.together.domain.ClubVideo;
import com.project.together.domain.File;
import com.project.together.domain.Match;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MainMapper {
    List<ClubVideo> getAllClubVideos();

    List<File> findFilesByTableGb(String sectionClubPhoto);

    List<Match> findK5MatchListByLeague(String k5);

    List<Match> findK7MatchListByLeague(String k7);

    List<Match> findW1MatchListByLeague(String w1);
}