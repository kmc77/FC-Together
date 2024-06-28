package com.project.together.mapper;

import com.project.together.domain.*;
import com.project.together.domain.Popup;
import org.apache.ibatis.annotations.Mapper;

import javax.swing.*;
import java.util.List;

@Mapper
public interface MainMapper {
    List<ClubVideo> getAllClubVideos();

    List<File> findFilesByTableGb(String sectionClubPhoto);

    List<Match> findK5MatchListByLeague(String k5);

    List<Match> findK7MatchListByLeague(String k7);

    List<Match> findW1MatchListByLeague(String w1);

    List<Ranking> getAllRankings();

    List<Trophy> getTrophy();

    List<Popup> getAllPopups();
}