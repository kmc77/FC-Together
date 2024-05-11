package com.project.together.controller;

import com.project.together.domain.Match;
import com.project.together.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @GetMapping("/match/matchpage")
    public String matchResult(Model model, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {
        List<Match> K5matchSchedule = matchService.K5selectSchedule();
        List<Match> K5matchResult = matchService.K5selectResult();
        List<Match> K7matchSchedule = matchService.K7selectSchedule();
        List<Match> K7matchResult = matchService.K7selectResult();
        List<Match> W1matchSchedule = matchService.W1selectSchedule();
        List<Match> W1matchResult = matchService.W1selectResult();

        List<Match> paginatedK5Schedule = getPaginatedList(K5matchSchedule, page, size);
        List<Match> paginatedK5Result = getPaginatedList(K5matchResult, page, size);
        List<Match> paginatedK7Schedule = getPaginatedList(K7matchSchedule, page, size);
        List<Match> paginatedK7Result = getPaginatedList(K7matchResult, page, size);
        List<Match> paginatedW1Schedule = getPaginatedList(W1matchSchedule, page, size);
        List<Match> paginatedW1Result = getPaginatedList(W1matchResult, page, size);

        model.addAttribute("K5Schedulelist", paginatedK5Schedule);
        model.addAttribute("K5Resultlist", paginatedK5Result);
        model.addAttribute("K7Schedulelist", paginatedK7Schedule);
        model.addAttribute("K7Resultlist", paginatedK7Result);
        model.addAttribute("W1Schedulelist", paginatedW1Schedule);
        model.addAttribute("W1Resultlist", paginatedW1Result);
        model.addAttribute("currentPage", page);

        return "layout/match/matchpage";
    }

    private List<Match> getPaginatedList(List<Match> list, int page, int size) {
        int startIndex = (page - 1) * size;
        int endIndex = Math.min(startIndex + size, list.size());
        if (startIndex > endIndex) {
            startIndex = endIndex;
        }
        return list.subList(startIndex, endIndex);
    }
}