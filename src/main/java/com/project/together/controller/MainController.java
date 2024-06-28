package com.project.together.controller;

import com.project.together.domain.*;
import com.project.together.domain.Popup;
import com.project.together.service.FileService;
import com.project.together.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.swing.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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
        List<Popup> popups = mainService.getAllPopups();

        // 현재 날짜를 가져옵니다.
        LocalDate currentDate = LocalDate.now();

        // 현재 날짜가 게시 날짜와 종료 날짜 사이에 있는 팝업만 필터링합니다.
        List<Popup> validPopups = popups.stream()
                .filter(popup -> !popup.getPopupStartDate().isAfter(currentDate) && !popup.getPopupEndDate().isBefore(currentDate))
                .collect(Collectors.toList());

        // 비디오 목록을 최대 5개까지만 자릅니다.
        if (clubVideos.size() > 5) {
            clubVideos = clubVideos.subList(0, 5);
        }

        model.addAttribute("sliderImages", sliderImages);
        model.addAttribute("clubVideos", clubVideos);
        model.addAttribute("popups", validPopups); // 유효한 팝업만 모델에 추가합니다.
        model.addAttribute("instagramUserId", instagramUserId);
        model.addAttribute("instagramAccessToken", instagramAccessToken);

        return "main"; // 메인 페이지의 Thymeleaf 템플릿 이름을 반환합니다.
    }


    // K5 매치목록 가져오기
    @GetMapping({"/", "/mainMatch/getK5MatchList"})
    public ResponseEntity<List<Match>> getK5MatchList() {
        List<Match> k5Match = mainService.findK5MatchListByLeague("k5");
        return ResponseEntity.ok(k5Match);
    }

    // K7 리그 매치 목록 가져오기
    @GetMapping("/mainMatch/getK7MatchList")
    public ResponseEntity<List<Match>> getK7MatchList() {
        List<Match> k7Matches = mainService.findK7MatchListByLeague("k7");
        return ResponseEntity.ok(k7Matches);
    }

    // W1 리그 매치 목록 가져오기
    @GetMapping("/mainMatch/getW1MatchList")
    public ResponseEntity<List<Match>> getW1MatchList() {
        List<Match> w1Matches = mainService.findW1MatchListByLeague("w1");
        return ResponseEntity.ok(w1Matches);
    }

    // 리그 순위 목록 가져오기
    @GetMapping("/mainMatch/getLeagueRankings")
    public ResponseEntity<List<Ranking>> getLeagueRankings() {
        List<Ranking> rankings = mainService.getAllRankings();
        return ResponseEntity.ok(rankings);
    }

    // 수상 내역 가져오기
    @GetMapping("/mainTrophy/getTrophy")
    public ResponseEntity<List<Trophy>> getTrophy() {
        System.out.println("================ mainService = " + mainService);
        List<Trophy> trophies = mainService.getTrophy();
        return ResponseEntity.ok(trophies);
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