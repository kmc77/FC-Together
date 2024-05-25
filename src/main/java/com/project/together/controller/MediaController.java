package com.project.together.controller;

import com.project.together.domain.*;
import com.project.together.service.MediaService;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/media")
public class MediaController {

    private final MediaService mediaService;

    @Autowired
    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    // 전체 페이지
    @GetMapping("/all")
    public String allPage() {
        return "/layout/media/all";
    }

    // 공지사항 페이지
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MEMBER')")
    @GetMapping("/notice")
    public String noticePage() {
        return "/layout/media/notice";
    }

    // 구단소식 페이지
    @GetMapping("/news")
    public String newsPage() {
        return "/layout/media/news";
    }

    // 경기사진 페이지
    @GetMapping("/photo")
    public String photoPage() {
        return "/layout/media/photo";
    }

    // 구단영상 페이지
    @GetMapping("/video")
    public String videoPage() {
        return "/layout/media/video";
    }


    // 공지사항 목록 조회
    @GetMapping("/notice/list")
    public ResponseEntity<List<Notice>> getNoticeList(@RequestParam(defaultValue = "0") int start, @RequestParam(defaultValue = "8") int limit) {
        Map<String, Integer> params = new HashMap<>();
        params.put("start", start);
        params.put("limit", limit);
        List<Notice> noticeList = mediaService.findAll(params);
        return new ResponseEntity<>(noticeList, HttpStatus.OK);
    }


    // 공지사항 상세보기 페이지
    @GetMapping("/noticeview")
    public String noticeViewPage(@RequestParam("no") int noticeNum, Model model) throws NotFoundException {
        mediaService.increaseNoticeHits(noticeNum); // 조회수 증가
        Notice notice = mediaService.getNotice(noticeNum);
        if (notice == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Notice not found with id: " + noticeNum);
        }

        // 공지사항 번호에 해당하는 파일 목록 조회
        List<File> files = mediaService.findFilesByNoticeNum(noticeNum);

        model.addAttribute("notice", notice);
        model.addAttribute("files", files); // 파일 목록을 모델에 추가

        Notice prevNotice = mediaService.findPrevNoticeByNoticeNum(noticeNum);
        System.out.println("======== 컨트롤러 prevNotice = " + prevNotice);
        if (prevNotice != null) {
            model.addAttribute("prevNotice", prevNotice);
        }

        return "layout/media/noticeview";
    }


    // 구단 뉴스 목록 조회
    @GetMapping("/news/list")
    public ResponseEntity<List<News>> getNewsList(@RequestParam(defaultValue = "0") int start, @RequestParam(defaultValue = "9") int limit) {
        Map<String, Integer> params = new HashMap<>();
        params.put("start", start);
        params.put("limit", limit);

        List<News> newsList = mediaService.getNewsList(params);
        return new ResponseEntity<>(newsList, HttpStatus.OK);
    }


    // 구단 뉴스 상세보기 페이지
    @GetMapping("/newsview")
    public String newsViewPage(@RequestParam("no") int newsNum, Model model) throws NotFoundException {

        mediaService.increaseNewsHits(newsNum); //조회수 증가
        News news = mediaService.newsViewPage(newsNum);

        if (news == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "News not found with id: " + newsNum);
        }

        model.addAttribute("news", news);

        News prevNews = mediaService.findPrevNewsByNewsNum(newsNum);
        if (prevNews != null) {
            model.addAttribute("prevNews", prevNews);
        }

        return "layout/media/newsview";
    }


    // 구단 사진 목록 조회
    @GetMapping("/photo/list")
    public ResponseEntity<List<ClubPhoto>> getClubPhotoList(@RequestParam(defaultValue = "0") int start, @RequestParam(defaultValue = "9") int limit) {
        Map<String, Integer> params = new HashMap<>();
        params.put("start", start);
        params.put("limit", limit);

        List<ClubPhoto> photoList = mediaService.getClubPhotoList(params);

        return new ResponseEntity<>(photoList, HttpStatus.OK);
    }


    @GetMapping("/photoview")
    public String photoViewPage(@RequestParam("no") int cpIdx, Model model) throws NotFoundException {
        mediaService.increasePhotoHits(cpIdx); // 조회수 증가
        // 현재 사진 조회
        ClubPhoto clubPhoto = mediaService.photoViewPage(cpIdx);
        if (clubPhoto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ClubPhoto not found with id: " + cpIdx);
        }
        model.addAttribute("clubPhoto", clubPhoto);

        // 이전 사진 조회
        ClubPhoto prevClubPhoto = mediaService.findPrevClubPhotoByCpIdx(cpIdx);
        if (prevClubPhoto != null) {
            model.addAttribute("prevClubPhoto", prevClubPhoto);
        }

        return "layout/media/photoview";
    }

    // 구단 영상 목록 조회
    @GetMapping("/video/list")
    public ResponseEntity<List<ClubVideo>> getClubVideoList(@RequestParam(defaultValue = "0") int start, @RequestParam(defaultValue = "9") int limit) {
        Map<String, Integer> params = new HashMap<>();
        params.put("start", start);
        params.put("limit", limit);

        List<ClubVideo> videoList = mediaService.getClubVideoList(params);

        return new ResponseEntity<>(videoList, HttpStatus.OK);
    }

    // 구단 영상 상세보기 페이지
    @GetMapping("/videoView")
    public String videoViewPage(@RequestParam("no") int cvIdx, Model model) throws NotFoundException {

        mediaService.increaseVideoHits(cvIdx);
        ClubVideo clubVideo = mediaService.videoViewPage(cvIdx);
        model.addAttribute("clubVideo", clubVideo);

        // 이전 영상 조회
        ClubVideo prevClubVideo = mediaService.findPrevClubVideoByCvIdx(cvIdx);
        if (prevClubVideo != null) {
            model.addAttribute("prevClubVideo", prevClubVideo);
        }

        return "layout/media/videoview";
    }
}
