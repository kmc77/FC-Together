package com.project.together.controller;

import com.project.together.domain.K5_Player;
import com.project.together.domain.K7_Player;
import com.project.together.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/team")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }


    // K5 선수 정보 가져오기
    @GetMapping("/layout/get_k5PlayerInfo")
    public ResponseEntity<List<K5_Player>> get_k5PlayerInfo() {
        List<K5_Player> k5Players = teamService.getK5Player();
        System.out.println("k5Players = " + k5Players);
        return ResponseEntity.ok(k5Players);
    }

    // K7 선수 정보 가져오기
    @GetMapping("/layout/get_k7PlayerInfo")
    public ResponseEntity<List<K7_Player>> get_k7PlayerInfo() {
        List<K7_Player> k7Players = teamService.getK7Player();
        System.out.println("k7Players = " + k7Players);
        return ResponseEntity.ok(k7Players);
    }

    /*// W1리그 선수 정보 가져오기
    @GetMapping("/layout/get_w1PlayerInfo")
    public ResponseEntity<List<W1_Player>> get_w1PlayerInfo() {
        List<W1_Player> w1Players = teamService.getW1Player();
        System.out.println("w1Players = " + w1Players);
        return ResponseEntity.ok(w1Players);
    }

    // TeamStaff 정보 가져오기
    @GetMapping("/layout/get_t1PlayerInfo")
    public ResponseEntity<List<TeamStaff>> get_t1PlayerInfo() {
        List<TeamStaff> teamStaffs = teamService.getTeamStaff();
        System.out.println("teamStaffs = " + teamStaffs);
        return ResponseEntity.ok(teamStaffs);
    }
*/


}
