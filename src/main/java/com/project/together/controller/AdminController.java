package com.project.together.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {


    @GetMapping("/layout/adminpage")
    public String page(Model model) {
        model.addAttribute("content", "페이지 레이아웃 입장");
        return "layout/adminpage";
    }

}
