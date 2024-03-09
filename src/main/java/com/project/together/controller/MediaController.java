package com.project.together.controller;

import com.project.together.domain.ClubPhoto;
import com.project.together.domain.ClubVideo;
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
        Notice notice = mediaService.getNotice(noticeNum);
        model.addAttribute("notice", notice);
        return "/layout/media/noticeview";
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

        News news = mediaService.newsViewPage(newsNum);
        model.addAttribute("news", news);

        // 이전 뉴스 불러오기
        if (newsNum > 1) { // 첫 번째 뉴스가 아닐 경우에만 이전 뉴스를 찾습니다.
            try {
                News prevNews = mediaService.newsViewPage(newsNum - 1);
                model.addAttribute("prevNews", prevNews);
            } catch (NotFoundException e) {
                // newsNum -1에 해당하는 사진이 없을 경우 이전 사진을 불러오지 않습니다.
            }
        }

        return "/layout/media/newsview";
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

        ClubPhoto clubPhoto = mediaService.photoViewPage(cpIdx);
        model.addAttribute("clubPhoto", clubPhoto);

        System.out.println("컨트롤러 clubPhoto = " + clubPhoto);
        // 이전글 불러오기
        if (cpIdx > 1) { // 첫 번째 뉴스가 아닐 경우만 이전 뉴스를 찾는다.
            try {
                ClubPhoto prevPhoto = mediaService.photoViewPage(cpIdx - 1);
                model.addAttribute("prevPhoto", prevPhoto);
            } catch (NotFoundException e) {
                // cpIdx -1에 해당하는 사진이 없을 경우 이전 사진 불러오지 않는다.
            }
        }

        return "/layout/media/photoview";
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

        ClubVideo clubVideo = mediaService.videoViewPage(cvIdx);
        model.addAttribute("clubVideo", clubVideo);

        System.out.println("컨트롤러 clubVideo = " + clubVideo);
        // 이전글 불러오기
        if (cvIdx > 1) { // 첫 번째 뉴스가 아닐 경우만 이전 뉴스를 찾는다.
            try {
                ClubVideo prevVideo = mediaService.videoViewPage(cvIdx - 1);
                model.addAttribute("prevVideo", prevVideo);
            } catch (NotFoundException e) {
                // cvIdx -1에 해당하는 사진이 없을 경우 이전 사진 불러오지 않는다.
            }
        }

        return "/layout/media/videoview";
    }



    // 미디어 전체 목록 조회
    @GetMapping("/all/list")
    public ResponseEntity<?> getAllMedia(@RequestParam(value = "start", defaultValue = "0") int start,
                                         @RequestParam(value = "limit", defaultValue = "10") int limit) {
        try {
            Map<String, Object> response = new HashMap<>();

            // 예시로 각 카테고리 별로 데이터를 가져오는 메서드를 호출합니다. 실제 구현은 데이터베이스 조회 등을 포함할 것입니다.
            List<ClubPhoto> photos = mediaService.getPhotos(start, limit);
          /*  List<News> news = mediaService.getNews(start, limit);
            List<Notice> notices = mediaService.getNotices(start, limit);
            List<ClubVideo> videos = mediaService.getVideos(start, limit);*/


            response.put("photos", photos);
         /*   response.put("news", news);
            response.put("notices", notices);
            response.put("videos", videos);*/

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 예외 처리
            Map<String, String> error = new HashMap<>();
            error.put("message", "정보를 가져오는데 실패했습니다.");
            return ResponseEntity.badRequest().body(error);
        }
    }

}