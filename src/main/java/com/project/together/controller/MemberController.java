package com.project.together.controller;

import com.project.together.domain.Member;
import com.project.together.domain.TeamStaff;
import com.project.together.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/JoinForm")
    public String showJoinForm() {
        return "member/JoinForm";
    }

    @PostMapping("/join")
    public String joinMember(Member member) {
        // 회원 가입 처리 로직 구현
        memberService.joinMember(member);
        return "redirect:/member/list";
    }

    @GetMapping("/list")
    public String getAllMembers(Model model) {
        // 회원 목록 조회 로직 구현
        List<Member> memberList = memberService.getAllMembers();
        model.addAttribute("memberList", memberList);
        return "member/MemberList";
    }
}


