package com.project.together.controller;

import com.project.together.domain.*;
import com.project.together.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/team")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }


    // k5, k7, w1 정보 가져오기
    @GetMapping("/layout/get_allPlayerInfo")
    public ResponseEntity<Map<String, List<?>>> get_allPlayerInfo() {
        Map<String, List<?>> allPlayersData = new HashMap<>();
        allPlayersData.put("k5", teamService.getK5Players());
        allPlayersData.put("k7", teamService.getK7Players());
        allPlayersData.put("w1", teamService.getW1Players());
        System.out.println("컨트롤러 allPlayersData = " + allPlayersData);
        return ResponseEntity.ok(allPlayersData);
    }


    // TeamStaff 정보 가져오기
    @GetMapping("/layout/get_teamStaffInfo")
    public ResponseEntity<List<TeamStaff>> get_coachingStaffInfo() {
        List<TeamStaff> teamStaffs = teamService.getTeamStaff();
        System.out.println("teamStaffs = " + teamStaffs);
        return ResponseEntity.ok(teamStaffs);
    }



}
