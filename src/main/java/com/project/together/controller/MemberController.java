package com.project.together.controller;

import com.project.together.domain.Member;
import com.project.together.domain.TeamStaff;
import com.project.together.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/member/JoinForm2")
    public String showJoinForm() {
        return "member/JoinForm2";
    }

}
