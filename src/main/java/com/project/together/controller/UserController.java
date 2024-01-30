package com.project.together.controller;

import com.project.together.domain.*;
import com.project.together.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;

    }

    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "어드민 페이지입니다.";
    }

    //@PostAuthorize("hasRole('ROLE_MANAGER')")
    //@PreAuthorize("hasRole('ROLE_MANAGER')")
    @Secured("ROLE_MANAGER")
    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "매니저 페이지입니다.";
    }

    @GetMapping("/LoginForm")
    public String showLoginForm() {
        return "user/LoginForm";
    }


    @GetMapping("/JoinForm")
    public String showJoinForm() {
        return "user/JoinForm";
    }


    @ResponseBody
    @GetMapping("/idCheck")
    public int idCheck(@RequestParam("username") String username) {
        return userService.idCheck(username);
    }

    @ResponseBody
    @GetMapping("/emailCheck")
    public int emailCheck(@RequestParam("user_email") String userEmail) {
        return userService.emailCheck(userEmail);
    }

    @ResponseBody
    @GetMapping("/players")
    public ResponseEntity<?> getPlayers(@RequestParam("selectLeague") String league) {
        if (league.equals("K5_Player")) {
            List<K5_Player> playerList = userService.getK5Players();
            return ResponseEntity.ok(playerList);
        } else if (league.equals("K7_Player")) {
            List<K7_Player> playerList = userService.getK7Players();
            return ResponseEntity.ok(playerList);
        } else if (league.equals("S_Player")) {
            List<S_Player> playerList = userService.getSPlayers();
            return ResponseEntity.ok(playerList);
        } else {
            return ResponseEntity.badRequest().body("Invalid league parameter");
        }
    }


    @PostMapping("/join")
    public String joinUser(User user, Model model) {
        userService.joinUser(user);
        System.out.println("회원가입 성공 = " + user);

        model.addAttribute("message", "회원가입이 완료되었습니다. 로그인해주세요."); // 메시지를 모델에 추가

        return "redirect:/user/LoginForm";
    }

//일반 로그인 로직
 /*   @PostMapping("/login")
    public ModelAndView loginMember(@RequestParam("member_id") String member_id,
                                    @RequestParam("member_pw") String member_pw,
                                    HttpSession session,
                                    RedirectAttributes rattr) {
        System.out.println("member_id = " + member_id);
        System.out.println("member_pw = " + member_pw);

        boolean loginSuccess = memberService.authenticateMember(member_id, member_pw);
        System.out.println("로그인 성공 여부 = " + loginSuccess + "!!");

        if (loginSuccess) {
            session.setAttribute("userId", member_id);
            ModelAndView modelAndView = new ModelAndView("redirect:/");
            return modelAndView;
        } else {
            rattr.addFlashAttribute("error", "아이디 또는 비밀번호가 일치하지 않습니다.");
            return new ModelAndView("redirect:/member/LoginForm");
        }
    }*/


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // 세션을 초기화하여 로그아웃 처리
        return "redirect:/"; // 로그인 폼으로 이동
    }


}


