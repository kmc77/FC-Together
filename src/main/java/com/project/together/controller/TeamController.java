package com.project.together.controller;

import com.project.together.domain.TeamStaff;
import com.project.together.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class TeamController {
    @Autowired
    private final TeamService teamService;

    @GetMapping("/")
    public String test(Model model) {
        TeamStaff teamStaff = teamService.test();

        model.addAttribute("teamStaff", teamStaff);
        return "index";
    }
}
