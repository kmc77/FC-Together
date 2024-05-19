package com.project.together.service;

import com.project.together.domain.ClubVideo;
import com.project.together.mapper.MainMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainService {

    private final MainMapper mainMapper;

    public List<ClubVideo> getAllClubVideos() {
        return mainMapper.getAllClubVideos();
    }
}
