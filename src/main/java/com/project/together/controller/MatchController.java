package com.project.together.controller;

import com.project.together.domain.Match;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MatchController {


@GetMapping
@Transactional
    public String matchResult(Model model, Match match) {

    return "";
    }
}
