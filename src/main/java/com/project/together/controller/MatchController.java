package com.project.together.controller;

import com.project.together.domain.Match;
import com.project.together.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/match")
public class MatchController {

    private final MatchService matchService;


    @GetMapping("/layout/matches")
    public ResponseEntity<Map<String, List<?>>> get_allPlayerInfo() {
        Map<String, List<?>> allPlayersData = new HashMap<>();
        allPlayersData.put("k5Schedule", matchService.K5selectSchedule());
        allPlayersData.put("k5Result", matchService.K5selectResult());
        allPlayersData.put("k7Schedule", matchService.K7selectSchedule());
        allPlayersData.put("k7Result", matchService.K7selectResult());
        allPlayersData.put("W1Schedule", matchService.W1selectSchedule());
        allPlayersData.put("W1Result", matchService.W1selectResult());
        System.out.println("컨트롤러 allPlayersData = " + allPlayersData);
        return ResponseEntity.ok(allPlayersData);
    }
}