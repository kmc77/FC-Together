package com.project.together.controller;

import com.project.together.domain.ClubVideo;
import com.project.together.domain.File;
import com.project.together.domain.Match;
import com.project.together.service.FileService;
import com.project.together.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final MainService mainService;


    @Value("${instagram.user.id}")
    private String instagramUserId;

    @Value("${instagram.access.token}")
    private String instagramAccessToken;

    @GetMapping({"/", ""})
    public String mainpage(Model model) {
        List<File> sliderImages = mainService.getImagesForSectionClubPhoto();
        List<ClubVideo> clubVideos = mainService.getAllClubVideos();

        // 비디오 목록을 최대 5개까지만 자르기
        if (clubVideos.size() > 5) {
            clubVideos = clubVideos.subList(0, 5);
        }

        model.addAttribute("sliderImages", sliderImages);
        model.addAttribute("clubVideos", clubVideos);
        model.addAttribute("instagramUserId", instagramUserId);
        model.addAttribute("instagramAccessToken", instagramAccessToken);

        return "main"; // 메인 페이지의 Thymeleaf 템플릿 이름을 반환
    }


    // 매치목록 가져오기
    @GetMapping({"/", "/mainMatch/getK5MatchList"})
    public ResponseEntity<List<Match>> getK5MatchList() {
        List<Match> k5Match = mainService.findK5MatchListByLeague("k5");
        return ResponseEntity.ok(k5Match);
    }



    @GetMapping("/club/clubpage")
    public String clubpage() {
        return "/layout/club/clubpage";
    }

/*    @GetMapping("/club/togetherpeople")
    public String togetherpeople() {
        return "/layout/club/togetherpeople";
    }*/

    @GetMapping("/team/teampage")
    public String teampage() {
        return "/layout/team/teampage";
    }
//
//    @GetMapping("/match/matchpage")
//    public String matchpage() {
//        return "/layout/match/matchpage";
//    }

    @GetMapping("/history/historypage")
    public String historypage() {
        return "/layout/history/historypage";
    }





}