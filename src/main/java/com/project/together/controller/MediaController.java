package com.project.together.controller;

import com.project.together.domain.Notice;
import com.project.together.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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
    public ResponseEntity<List<Notice>> getNoticeList() {
        List<Notice> noticeList = mediaService.findAll();
        return new ResponseEntity<>(noticeList, HttpStatus.OK);
    }
}
