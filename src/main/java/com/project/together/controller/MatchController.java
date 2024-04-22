package com.project.together.controller;

import com.project.together.domain.Match;
import com.project.together.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @GetMapping("/match/matchpage")
    public String matchResult(Model model) {
        List<Match> K5matchSchedule = matchService.K5selectSchedule();
        List<Match> K5matchResult = matchService.K5selectResult();
        List<Match> K7matchSchedule = matchService.K7selectSchedule();
        List<Match> K7matchResult = matchService.K7selectResult();
        System.out.println("K5match" + K5matchSchedule);
        System.out.println("K7match" + K7matchSchedule);
        System.out.println("K5match" + K5matchResult);
        System.out.println("K7match" + K7matchResult);
        model.addAttribute("K5Schedulelist", K5matchSchedule);
        model.addAttribute("K5Resultlist", K5matchResult);
        model.addAttribute("K7Schedulelist", K7matchSchedule);
        model.addAttribute("K7Resultlist", K7matchResult);
        return "layout/match/matchpage";
    }
}