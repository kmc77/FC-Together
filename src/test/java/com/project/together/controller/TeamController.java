package com.project.together.controller;

import com.project.together.domain.TeamStaff;
import com.project.together.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @GetMapping("/")
    public String getAllTeamStaff(Model model) {
        TeamStaff teamStaffList = teamService.getAllTeamStaff();
        System.out.println("asdf"+ teamStaffList);
        model.addAttribute("teamStaffList", teamStaffList);
        return "index";
    }
}
