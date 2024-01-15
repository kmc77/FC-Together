package com.project.together.controller;

import com.project.together.domain.K5_Player;
import com.project.together.domain.K7_Player;
import com.project.together.domain.Member;
import com.project.together.domain.S_Player;
import com.project.together.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
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


    @ResponseBody
    @GetMapping("/idCheck")
    public int idCheck(@RequestParam("memberId") String memberId) {
        return memberService.idCheck(memberId);
    }

    @ResponseBody
    @GetMapping("/emailCheck")
    public int emailCheck(@RequestParam("memberEmail") String memberEmail) {
        return memberService.emailCheck(memberEmail);
    }

    @ResponseBody
    @GetMapping("/players")
    public ResponseEntity<?> getPlayers(@RequestParam("selectLeague") String league) {
        if (league.equals("K5_Player")) {
            List<K5_Player> playerList = memberService.getK5Players();
            return ResponseEntity.ok(playerList);
        } else if (league.equals("K7_Player")) {
            List<K7_Player> playerList = memberService.getK7Players();
            return ResponseEntity.ok(playerList);
        } else if (league.equals("S_Player")) {
            List<S_Player> playerList = memberService.getSPlayers();
            return ResponseEntity.ok(playerList);
        } else {
            return ResponseEntity.badRequest().body("Invalid league parameter");
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


