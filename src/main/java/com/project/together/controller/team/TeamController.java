package com.project.together.controller.team;

import com.project.together.domain.teamstaff.TeamStaff;
import com.project.together.service.team.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;

    @GetMapping("/test")
    public String test(Model model) {
        TeamStaff teamStaff = teamService.test();
        System.out.println("test: " + teamStaff);

        // Add the TeamStaff object to the model
        model.addAttribute("teamStaff", teamStaff);
        return "index";
    }
}
