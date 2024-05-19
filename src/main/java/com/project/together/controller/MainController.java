package com.project.together.controller;

import com.project.together.domain.ClubVideo;
import com.project.together.domain.File;
import com.project.together.service.FileService;
import com.project.together.service.MainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final FileService fileService;
    private final MainService mainService;


    /*@GetMapping({"/", ""})
    public String mainpage(Model model) {
        List<File> sliderImages = fileService.getImagesForSectionClubPhoto();
        model.addAttribute("sliderImages", sliderImages);
        return "main"; // 메인 페이지의 Thymeleaf 템플릿 이름을 반환
    }*/

    @GetMapping({"/", ""})
    public String mainpage(Model model) {
        List<File> sliderImages = fileService.getImagesForSectionClubPhoto();
        List<ClubVideo> clubVideos = mainService.getAllClubVideos();
        model.addAttribute("sliderImages", sliderImages);
        model.addAttribute("clubVideos", clubVideos);
        return "main"; // 메인 페이지의 Thymeleaf 템플릿 이름을 반환
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