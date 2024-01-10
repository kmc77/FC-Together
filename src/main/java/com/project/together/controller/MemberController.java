package com.project.together.controller;

import com.project.together.domain.Member;
import com.project.together.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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


    @GetMapping("/idCheck")
    public ResponseEntity<String> idCheck(@RequestParam String memberId) {
        boolean isIdAvailable = memberService.idCheck(memberId);
        if (isIdAvailable) {
            return ResponseEntity.ok("해당 ID는 사용 가능합니다.");
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("해당 ID는 이미 사용 중입니다.");
        }
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


