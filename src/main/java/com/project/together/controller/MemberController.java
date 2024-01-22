package com.project.together.controller;

import com.project.together.config.jwt.AuthenticationResponse;
import com.project.together.config.jwt.JwtUtil;
import com.project.together.config.jwt.MyUserDetailsService;
import com.project.together.domain.*;
import com.project.together.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import com.project.together.domain.AuthenticationRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService myUserDetailsService;
    private final JwtUtil jwtUtil;

    @Autowired
    public MemberController(MemberService memberService, BCryptPasswordEncoder bCryptPasswordEncoder,
                            AuthenticationManager authenticationManager, MyUserDetailsService myUserDetailsService,
                            JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticationManager = authenticationManager;
        this.myUserDetailsService = myUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/JoinForm")
    public String showJoinForm() {
        return "member/JoinForm";
    }

    @GetMapping("/LoginForm")
    public String showLoginForm() {
        return "member/LoginForm";
    }

    @ResponseBody
    @GetMapping("/idCheck")
    public int idCheck(@RequestParam("member_id") String memberId) {
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
    public String joinMember(Member member, Model model) {
        memberService.joinMember(member);
        System.out.println("회원가입 성공 = " + member);

        model.addAttribute("message", "회원가입이 완료되었습니다. 로그인해주세요."); // 메시지를 모델에 추가

        return "redirect:/member/LoginForm";
    }


   /* @PostMapping("/login")
    public ModelAndView loginMember(@RequestParam("member_id") String id,
                                    @RequestParam("member_pw") String password,
                                    HttpSession session,
                                    RedirectAttributes rattr) {
        boolean loginSuccess = memberService.authenticateMember(id, password);
        System.out.println("로그인 성공 여부 = " + loginSuccess + "!!");

        if (loginSuccess) {
            session.setAttribute("userId", id);
            ModelAndView modelAndView = new ModelAndView("redirect:/");
            return modelAndView;
        } else {
            rattr.addFlashAttribute("error", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return new ModelAndView("redirect:/member/LoginForm");
        }
    }*/

/*
    @PostMapping("/login")
    public ResponseEntity<TokenInfo> login(@RequestBody AuthenticationRequest request) {
        String member_id = request.getMember_id();
        String member_pw = request.getMember_pw();
        TokenInfo tokenInfo = memberService.login(member_id, member_pw);

        // 토큰 값 로그 출력
        System.out.println("ㄴㄴToken: " + tokenInfo.getAccessToken());

        return ResponseEntity.ok(tokenInfo);
    }
*/

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        System.out.println("컨트롤러 authenticationRequest = " + authenticationRequest);
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getMember_id(), authenticationRequest.getMember_pw())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }


        final UserDetails userDetails = myUserDetailsService
                .loadUserByUsername(authenticationRequest.getMember_id());
        System.out.println("컨트롤러 userDetails = " + userDetails);
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(jwt));
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션을 초기화하여 로그아웃 처리
        return "redirect:/"; // 로그인 폼으로 이동
    }


    @GetMapping("/list")
    public String getAllMembers(Model model) {
        // 회원 목록 조회 로직 구현
        List<Member> memberList = memberService.getAllMembers();
        model.addAttribute("memberList", memberList);
        return "member/MemberList";
    }
}


