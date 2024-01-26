package com.project.together.controller;

import com.project.together.domain.TeamStaff;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    @GetMapping("/m")
    public String getAllTeamStaff() {
        return "main";
    }
}
