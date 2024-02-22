package com.project.together.service;

import com.project.together.domain.News;
import com.project.together.domain.Notice;
import com.project.together.mapper.MediaMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MediaService {

    private final MediaMapper mediaMapper;

    public List<Notice> findAll(Map<String, Integer> params) {
        return mediaMapper.findAll(params);
    }



    // 공지사항 상세보기 페이지
    public Notice getNotice(int noticeNum) throws NotFoundException {
        Notice notice = mediaMapper.findNoticeByNoticeNum(noticeNum);
        if (notice == null) {
            throw new NotFoundException(noticeNum + "번호의 공지 사항을 찾을 수 없습니다.");
        }
        return notice;
    }

    public List<News> getNewsList(Map<String, Integer> params) {
        return mediaMapper.getNewsList(params);
    }
}

