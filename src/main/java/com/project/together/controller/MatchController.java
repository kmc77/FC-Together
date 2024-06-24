package com.project.together.controller;

import com.project.together.domain.Match;
import com.project.together.domain.Team;
import com.project.together.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @GetMapping("/match/matchpage")
    public String matchResult(Model model, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "5") int size) {
        List<Match> K5matchSchedule = matchService.K5selectSchedule();
        List<Match> K5matchResult = matchService.K5selectResult();
        List<Team> K5leagueGb = matchService.K5leagueGb();

        List<Match> K7matchSchedule = matchService.K7selectSchedule();
        List<Match> K7matchResult = matchService.K7selectResult();
        List<Team> K7leagueGb = matchService.K7leagueGb();

        List<Match> W1matchSchedule = matchService.W1selectSchedule();
        List<Match> W1matchResult = matchService.W1selectResult();
        List<Team> W1leagueGb = matchService.W1leagueGb();


        // Set K5leagueGb data to each K5matchSchedule Match
        if (K5matchSchedule != null && K5leagueGb != null) {
            for (int i = 0; i < K5matchSchedule.size(); i++) {
                Match match = K5matchSchedule.get(i);
                // Ensure team list is initialized
                if (match.getTeam() == null) {
                    match.setTeam(new ArrayList<>());
                }
                // Add K5leagueGb team to the match's team list
                if (i < K5leagueGb.size()) {
                    match.getTeam().add(K5leagueGb.get(i));
                }
            }
        }

        if (K5matchResult != null && K5leagueGb != null) {
            for (int i = 0; i < K5matchResult.size(); i++) {
                Match match = K5matchResult.get(i);
                // Ensure team list is initialized
                if (match.getTeam() == null) {
                    match.setTeam(new ArrayList<>());
                }
                // Add K5leagueGb team to the match's team list
                if (i < K5leagueGb.size()) {
                    match.getTeam().add(K5leagueGb.get(i));
                }
            }
        }

        if (K7matchSchedule != null && K7leagueGb != null) {
            for (int i = 0; i < K7matchSchedule.size(); i++) {
                Match match = K7matchSchedule.get(i);
                // Ensure team list is initialized
                if (match.getTeam() == null) {
                    match.setTeam(new ArrayList<>());
                }
                // Add K5leagueGb team to the match's team list
                if (i < K7leagueGb.size()) {
                    match.getTeam().add(K7leagueGb.get(i));
                }
            }
        }

        if (K7matchResult != null && K7leagueGb != null) {
            for (int i = 0; i < K7matchResult.size(); i++) {
                Match match = K7matchResult.get(i);
                // Ensure team list is initialized
                if (match.getTeam() == null) {
                    match.setTeam(new ArrayList<>());
                }
                // Add K5leagueGb team to the match's team list
                if (i < K7leagueGb.size()) {
                    match.getTeam().add(K7leagueGb.get(i));
                }
            }
        }

        if (W1matchSchedule != null && W1leagueGb != null) {
            for (int i = 0; i < W1matchSchedule.size(); i++) {
                Match match = W1matchSchedule.get(i);
                // Ensure team list is initialized
                if (match.getTeam() == null) {
                    match.setTeam(new ArrayList<>());
                }
                // Add K5leagueGb team to the match's team list
                if (i < W1leagueGb.size()) {
                    match.getTeam().add(W1leagueGb.get(i));
                }
            }
        }

        if (W1matchResult != null && W1leagueGb != null) {
            for (int i = 0; i < W1matchResult.size(); i++) {
                Match match = W1matchResult.get(i);
                // Ensure team list is initialized
                if (match.getTeam() == null) {
                    match.setTeam(new ArrayList<>());
                }
                // Add K5leagueGb team to the match's team list
                if (i < W1leagueGb.size()) {
                    match.getTeam().add(W1leagueGb.get(i));
                }
            }
        }

        // Pagination logic
        List<Match> paginatedK5Schedule = getMatchPaginatedList(K5matchSchedule, page, size);
        List<Match> paginatedK5Result = getMatchPaginatedList(K5matchResult, page, size);
        List<Team> paginatedK5leagueGb = getTeamPaginatedList(K5leagueGb, page, size);

        List<Match> paginatedK7Schedule = getMatchPaginatedList(K7matchSchedule, page, size);
        List<Match> paginatedK7Result = getMatchPaginatedList(K7matchResult, page, size);
        List<Team> paginatedK7leagueGb = getTeamPaginatedList(K7leagueGb, page, size);


        List<Match> paginatedW1Schedule = getMatchPaginatedList(W1matchSchedule, page, size);
        List<Match> paginatedW1Result = getMatchPaginatedList(W1matchResult, page, size);
        List<Team> paginatedW1leagueGb = getTeamPaginatedList(W1leagueGb, page, size);

        model.addAttribute("K5Schedulelist", paginatedK5Schedule);
        model.addAttribute("K5Resultlist", paginatedK5Result);
        model.addAttribute("K5leagueGb", paginatedK5leagueGb);

        model.addAttribute("K7Schedulelist", paginatedK7Schedule);
        model.addAttribute("K7Resultlist", paginatedK7Result);
        model.addAttribute("K7leagueGb", paginatedK7leagueGb);

        model.addAttribute("W1Schedulelist", paginatedW1Schedule);
        model.addAttribute("W1Resultlist", paginatedW1Result);
        model.addAttribute("W1leagueGb", paginatedW1leagueGb);
        model.addAttribute("currentPage", page);

        return "layout/match/matchpage";
    }

    private List<Match> getMatchPaginatedList(List<Match> list, int page, int size) {
        int startIndex = (page - 1) * size;
        int endIndex = Math.min(startIndex + size, list.size());
        if (startIndex > endIndex) {
            startIndex = endIndex;
        }
        return list.subList(startIndex, endIndex);
    }

    private List<Team> getTeamPaginatedList(List<Team> list, int page, int size) {
        int startIndex = (page - 1) * size;
        int endIndex = Math.min(startIndex + size, list.size());
        if (startIndex > endIndex) {
            startIndex = endIndex;
        }
        return list.subList(startIndex, endIndex);
    }
}
