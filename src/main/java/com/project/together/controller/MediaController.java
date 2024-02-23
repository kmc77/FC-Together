package com.project.together.controller;

import com.project.together.domain.ClubPhoto;
import com.project.together.domain.News;
import com.project.together.domain.Notice;
import com.project.together.service.MediaService;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.awt.print.Pageable;
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
        return "/layout/info/all";
    }

    // 공지사항 페이지
    @GetMapping("/notice")
    public String noticePage() {
        return "/layout/info/notice";
    }

    // 구단소식 페이지
    @GetMapping("/news")
    public String newsPage() {
        return "/layout/info/news";
    }

    // 경기사진 페이지
    @GetMapping("/photo")
    public String photoPage() {
        return "/layout/info/photo";
    }

    // 구단영상 페이지
    @GetMapping("/video")
    public String videoPage() {
        return "/layout/info/video";
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
        Notice notice = mediaService.getNotice(noticeNum);
        model.addAttribute("notice", notice);
        return "/layout/info/noticeview";
    }

    // 구단뉴스 목록 조회
    @GetMapping("/news/list")
    public ResponseEntity<List<News>> getNewsList(@RequestParam(defaultValue = "0") int start, @RequestParam(defaultValue = "9") int limit) {
        Map<String, Integer> params = new HashMap<>();
        params.put("start", start);
        params.put("limit", limit);

        List<News> newsList = mediaService.getNewsList(params);
        System.out.println("컨트롤러 newsList = " + newsList);
        return new ResponseEntity<>(newsList, HttpStatus.OK);
    }

    //구단 뉴스 상세보기 페이지
    @GetMapping("/newsview")
    public String newsViewPage(@RequestParam("no") int noticeNum, Model model) throws NotFoundException {

        News news = mediaService.newsViewPage(noticeNum);
        System.out.println("컨트롤러 news = " + news);
        model.addAttribute("news", news);
        return "/layout/info/newsview";
    }


    // 구단사진 목록 조회
    @GetMapping("/photo/list")
    public ResponseEntity<List<ClubPhoto>> getClubPhotoList(@RequestParam(defaultValue = "0") int start, @RequestParam(defaultValue = "9") int limit) {
        Map<String, Integer> params = new HashMap<>();
        params.put("start", start);
        params.put("limit", limit);

        List<ClubPhoto> photoList = mediaService.getClubPhotoList(params);

        return new ResponseEntity<>(photoList, HttpStatus.OK);
    }

}
