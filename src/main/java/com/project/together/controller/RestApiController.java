package com.project.together.controller;

import java.util.List;

import com.project.together.config.auth.PrincipalDetails;
import com.project.together.domain.Member;
import com.project.together.mapper.MemberMapper;
import com.project.together.service.MemberService;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
// @CrossOrigin // CORS 허용
public class RestApiController {

    private final MemberService memberService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 모든 사람이 접근 가능
    @GetMapping("home")
    public String home() {
        return "<h1>home</h1>";
    }

    // Tip : JWT를 사용하면 UserDetailsService를 호출하지 않기 때문에 @AuthenticationPrincipal 사용
    // 불가능.
    // 왜냐하면 @AuthenticationPrincipal은 UserDetailsService에서 리턴될 때 만들어지기 때문이다.

    // 유저 혹은 매니저 혹은 어드민이 접근 가능
    @GetMapping("user")
    public String user(Authentication authentication) {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("principal : " + principal.getMember().getMember_id());
        System.out.println("principal : " + principal.getMember().getMember_name());
        System.out.println("principal : " + principal.getMember().getMember_pw());

        return "<h1>user</h1>";
    }

    // 매니저 혹은 어드민이 접근 가능
    @GetMapping("manager/reports")
    public String reports() {
        return "<h1>reports</h1>";
    }

    // 어드민이 접근 가능
    @GetMapping("admin/users")
    public List<Member> members() {
        return memberService.getAllMembers();
    }

    @PostMapping("join")
    public String join(Member member) {
        member.setMember_pw(bCryptPasswordEncoder.encode(member.getMember_pw()));
        member.setMember_roles("ROLE_USER");
        memberService.save(member);
        return "redirect:/member/LoginForm";
    }

}
