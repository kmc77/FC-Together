package com.project.together.service;

import com.project.together.domain.*;
import com.project.together.mapper.MainMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService {

    private final MainMapper mainMapper;

    /*@Cacheable(value = "clubVideosCache")*/
    public List<ClubVideo> getAllClubVideos() {
        return mainMapper.getAllClubVideos();
    }

    /*@Cacheable(value = "sliderImagesCache")*/
    public List<File> getImagesForSectionClubPhoto() {
        return mainMapper.findFilesByTableGb("sectionClubPhoto");
    }

    public List<Match> findK5MatchListByLeague(String k5) {
        return mainMapper.findK5MatchListByLeague(k5);
    }

    public List<Match> findK7MatchListByLeague(String k7) {
        return mainMapper.findK7MatchListByLeague(k7);
    }

    public List<Match> findW1MatchListByLeague(String w1) {
        return mainMapper.findW1MatchListByLeague(w1);
    }

    public List<Ranking> getAllRankings() {
        return mainMapper.getAllRankings();
    }

    public List<Trophy> getTrophy() {
        return mainMapper.getTrophy();
    }
}
