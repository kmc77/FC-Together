package com.project.together.controller;

import com.project.together.config.auth.PrincipalDetails;
import com.project.together.domain.*;
import com.project.together.service.MediaService;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
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
    /*@GetMapping("/all")
    public String allPage() {
        return "/layout/media/all";
    }*/

    // 공지사항 페이지
    @GetMapping("/notice")
    public String noticePage(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails != null) {
            System.out.println("User: " + principalDetails.getUsername());
        }
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
/*
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MANAGER', 'ROLE_ADMIN')")
*/
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
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID가 포함된 공지를 찾을 수 없습니다.: " + noticeNum);
        }

        model.addAttribute("notice", notice);

        Notice prevNotice = mediaService.findPrevNoticeByNoticeNum(noticeNum);
        if (prevNotice != null) {
            model.addAttribute("prevNotice", prevNotice);
        }

        return "layout/media/noticeview";
    }


    // 공지사항 상세 데이터 조회
    @GetMapping("/noticeview/data")
    public ResponseEntity<Map<String, Object>> getNoticeViewData(@RequestParam("no") int noticeNum) throws NotFoundException {
        mediaService.increaseNoticeHits(noticeNum); // 조회수 증가
        Notice notice = mediaService.getNotice(noticeNum);
        if (notice == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID가 포함된 공지를 찾을 수 없습니다. : " + noticeNum);
        }

        List<File> files = mediaService.findFilesByNoticeNum(noticeNum);
        Notice prevNotice = mediaService.findPrevNoticeByNoticeNum(noticeNum);

        Map<String, Object> response = new HashMap<>();
        response.put("notice", notice);
        response.put("files", files);
        response.put("prevNotice", prevNotice);

        return new ResponseEntity<>(response, HttpStatus.OK);
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
        mediaService.increaseNewsHits(newsNum); // 조회수 증가
        News news = mediaService.newsViewPage(newsNum);

        if (news == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID가 포함된 뉴스를 찾을 수 없습니다.: " + newsNum);
        }

        model.addAttribute("news", news);

        News prevNews = mediaService.findPrevNewsByNewsNum(newsNum);
        if (prevNews != null) {
            model.addAttribute("prevNews", prevNews);
        }

        return "layout/media/newsview";
    }

    // 구단 뉴스 상세 데이터 조회
    @GetMapping("/newsview/data")
    public ResponseEntity<Map<String, Object>> getNewsViewData(@RequestParam("no") int newsNum) throws NotFoundException {
        mediaService.increaseNewsHits(newsNum); // 조회수 증가
        News news = mediaService.newsViewPage(newsNum);
        if (news == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ID가 포함된 뉴스를 찾을 수 없습니다.: " + newsNum);
        }

        News prevNews = mediaService.findPrevNewsByNewsNum(newsNum);

        Map<String, Object> response = new HashMap<>();
        response.put("news", news);
        response.put("prevNews", prevNews);

        return new ResponseEntity<>(response, HttpStatus.OK);
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


    // 구단 사진 상세보기 페이지
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

    // 구단 사진 상세 데이터 조회
    @GetMapping("/photoview/data")
    public ResponseEntity<Map<String, Object>> getPhotoViewData(@RequestParam("no") int cpIdx) throws NotFoundException {
        mediaService.increasePhotoHits(cpIdx); // 조회수 증가
        ClubPhoto clubPhoto = mediaService.photoViewPage(cpIdx);
        if (clubPhoto == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ClubPhoto not found with id: " + cpIdx);
        }

        ClubPhoto prevClubPhoto = mediaService.findPrevClubPhotoByCpIdx(cpIdx);

        Map<String, Object> response = new HashMap<>();
        response.put("clubPhoto", clubPhoto);
        response.put("prevClubPhoto", prevClubPhoto);

        return new ResponseEntity<>(response, HttpStatus.OK);
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
        if (clubVideo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ClubVideo not found with id: " + cvIdx);
        }
        model.addAttribute("clubVideo", clubVideo);

        // 이전 영상 조회
        ClubVideo prevClubVideo = mediaService.findPrevClubVideoByCvIdx(cvIdx);
        if (prevClubVideo != null) {
            model.addAttribute("prevClubVideo", prevClubVideo);
        }

        return "layout/media/videoview";
    }

    // 구단 영상 상세 데이터 조회
    @GetMapping("/videoView/data")
    public ResponseEntity<Map<String, Object>> getVideoViewData(@RequestParam("no") int cvIdx) throws NotFoundException {
        mediaService.increaseVideoHits(cvIdx);
        ClubVideo clubVideo = mediaService.videoViewPage(cvIdx);
        if (clubVideo == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "ClubVideo not found with id: " + cvIdx);
        }

        ClubVideo prevClubVideo = mediaService.findPrevClubVideoByCvIdx(cvIdx);

        Map<String, Object> response = new HashMap<>();
        response.put("clubVideo", clubVideo);
        response.put("prevClubVideo", prevClubVideo);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
