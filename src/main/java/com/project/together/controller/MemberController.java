package com.project.together.controller;

import com.project.together.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

   /* @GetMapping("/")
    public void getMemberById() {

    }*/

}
