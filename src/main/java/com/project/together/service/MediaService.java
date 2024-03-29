package com.project.together.service;

import com.project.together.domain.ClubPhoto;
import com.project.together.domain.ClubVideo;
import com.project.together.domain.News;
import com.project.together.domain.Notice;
import com.project.together.mapper.MediaMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    // 뉴스 목록
    public List<News> getNewsList(Map<String, Integer> params) {
        return mediaMapper.getNewsList(params);
    }

    // 사진 목록
    public List<ClubPhoto> getClubPhotoList(Map<String, Integer> params) {
        return mediaMapper.getClubPhotoList(params);
    }

    //뉴스 상세보기 페이지
    public News newsViewPage(int newsNum) throws NotFoundException {
        News news = mediaMapper.findNewsByNewsNumber(newsNum);
        if (news == null) {
            throw new NotFoundException(newsNum + "번호의 공지 사항을 찾을 수 없습니다.");
        }
        return news;
    }

    // 구단 사진 상세보기 페이지
    public ClubPhoto photoViewPage(int cpIdx) throws NotFoundException {
        ClubPhoto clubPhoto = mediaMapper.findPhotosByPhotoNumber(cpIdx);
        if (clubPhoto == null) {
            throw new NotFoundException(cpIdx + "번호의 구단 사진을 찾을 수 없습니다.");
        }
        return clubPhoto;
    }

    // 영상 목록
    public List<ClubVideo> getClubVideoList(Map<String, Integer> params) {
        return mediaMapper.getVideoList(params);
    }

    //구단 영상 상세보기 페이지
    public ClubVideo videoViewPage(int cvIdx) throws NotFoundException {
        ClubVideo clubVideo = mediaMapper.findVideosByVideoNumber(cvIdx);
        if (clubVideo == null) {
            throw new NotFoundException(cvIdx + "번호의 구단 영상을 찾을 수 없습니다.");
        }
        return clubVideo;
    }

    public List<ClubPhoto> getPhotos(int start, int limit) {
        return mediaMapper.getPhotos(start, limit);
    }


    /* ============================================ */




}

