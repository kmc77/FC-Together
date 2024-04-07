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
        List<Match> matchResult = matchService.selectResult();
        List<Match> matchSchedule = matchService.selectSchedule();
        System.out.println("match" + matchResult);
        System.out.println("match" + matchSchedule);
        model.addAttribute("Resultlist", matchResult);
        model.addAttribute("Schedulelist", matchSchedule);
        return "layout/match/matchpage";
    }
}
