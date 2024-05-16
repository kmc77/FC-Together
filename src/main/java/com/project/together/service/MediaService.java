package com.project.together.service;

import com.project.together.domain.*;
import com.project.together.mapper.MediaMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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


    // 공지사항 이전글 찾기
    public Notice findPrevNoticeByNoticeNum(int noticeNum) {
        return mediaMapper.findPrevNoticeByNoticeNum(noticeNum);
    }


    // 공지사항 조회수 증가
    public void increaseNoticeHits(int noticeNum) throws NotFoundException {
        Notice notice = mediaMapper.findNoticeByNoticeNum(noticeNum);
        if (notice == null) {
            throw new NotFoundException("공지사항을 찾을 수 없습니다: " + noticeNum);
        }
        notice.setNoticeHits(notice.getNoticeHits() + 1);
        mediaMapper.updateNoticeHits(notice.getNoticeNum(), notice.getNoticeHits());
    }

    public List<File> findFilesByNoticeNum(int noticeNum) {
        return mediaMapper.findFilesByNoticeNum(noticeNum);
    }

    /*========================================*/

    // 뉴스 목록
    public List<News> getNewsList(Map<String, Integer> params) {
        return mediaMapper.getNewsList(params);
    }


    //뉴스 상세보기 페이지
    public News newsViewPage(int newsNum) throws NotFoundException {
        News news = mediaMapper.findNewsByNewsNumber(newsNum);
        if (news == null) {
            throw new NotFoundException(newsNum + "번호의 공지 사항을 찾을 수 없습니다.");
        }
        return news;
    }

    // 뉴스 조회수 증가
    public void increaseNewsHits(int newsNum) throws NotFoundException {
        News news = mediaMapper.findNewsByNewsNumber(newsNum);
        if (news == null) {
            throw new NotFoundException("뉴스를 찾을 수 없습니다: " + newsNum);
        }
        news.setNewsHits(news.getNewsHits() + 1);
        mediaMapper.updateNewsHits(news.getNewsIdx(), news.getNewsHits());
    }

    public News findPrevNewsByNewsNum(int newsNum) {
        return mediaMapper.findPrevNewsByNewsNum(newsNum);
    }


    /*========================================*/

    // 사진 목록
    public List<ClubPhoto> getClubPhotoList(Map<String, Integer> params) {
        return mediaMapper.getClubPhotoList(params);
    }

    // 구단 사진 상세보기 페이지
    public ClubPhoto photoViewPage(int cpIdx) throws NotFoundException {
        ClubPhoto clubPhoto = mediaMapper.findPhotosByPhotoNumber(cpIdx);
        if (clubPhoto == null) {
            throw new NotFoundException(cpIdx + "번호의 구단 사진을 찾을 수 없습니다.");
        }
        return clubPhoto;
    }

    // 사진 조회수 증가
    public void increasePhotoHits(int photoNum) throws NotFoundException {
        ClubPhoto photo = mediaMapper.findPhotosByPhotoNumber(photoNum);
        if (photo == null) {
            throw new NotFoundException("사진을 찾을 수 없습니다: " + photoNum);
        }
        photo.setCpHits(photo.getCpHits() + 1);
        mediaMapper.updatePhotoHits(photo.getCpIdx(), photo.getCpHits());
    }


    public ClubPhoto findPrevClubPhotoByCpIdx(int cpIdx) {
        return mediaMapper.findPrevClubPhotoByCpIdx(cpIdx);
    }



    /*========================================*/

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

    public void increaseVideoHits(int cvIdx) throws NotFoundException {
        ClubVideo clubVideo = mediaMapper.findVideosByVideoNumber(cvIdx);
        if (clubVideo == null) {
            throw new NotFoundException(cvIdx + "번 구단 영상을 찾을 수 없습니다.");
        }
        clubVideo.setCvHits(clubVideo.getCvHits() + 1); // 조회수 1 증가
        mediaMapper.updateVideoHits(clubVideo.getCvIdx(), clubVideo.getCvHits());
    }

    public ClubVideo findPrevClubVideoByCvIdx(int cvIdx) {
        return mediaMapper.findPrevClubVideoByCvIdx(cvIdx);
    }



















    /* ============================================ */




}

