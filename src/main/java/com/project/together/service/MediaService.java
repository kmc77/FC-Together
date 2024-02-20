package com.project.together.service;

import com.project.together.domain.Notice;
import com.project.together.mapper.MediaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final MediaMapper mediaMapper;

    public List<Notice> findAll() {
        // MediaMapper를 사용하여 모든 Notice 객체를 조회하고 이를 반환합니다.
        return mediaMapper.findAll();
    }
}

