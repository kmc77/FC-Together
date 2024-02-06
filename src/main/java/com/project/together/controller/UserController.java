package com.project.together.controller;

import com.project.together.config.auth.PrincipalDetails;
import com.project.together.config.jwt.JwtProperties;
import com.project.together.config.jwt.TokenUtils;
import com.project.together.domain.*;
import com.project.together.mapper.UserMapper;
import com.project.together.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;


    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping("/user")
    public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("principalDetails = " + principalDetails.getUser());
        return "user";
    }

    //쿠키와 로컬 스토리지에 토큰 값을 받는 로직. 이름 변경 예정임
    @GetMapping("/tokenAll")
    @ResponseBody
    public Map<String, String> tokenAll(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Map<String, String> response = new HashMap<>();
        response.put("username", principalDetails.getUsername());
        System.out.println("사용자 정보 response = " + response);
        return response;
    }

    @PostMapping("/refresh_token")
    public ResponseEntity<?> refreshToken(HttpServletRequest request) {
        // 쿠키에서 "refreshToken"의 값을 가져옴
        String refreshToken = Arrays.stream(request.getCookies())
                .filter(cookie -> JwtProperties.REFRESH_TOKEN_HEADER_STRING.trim().equals(cookie.getName()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null);

        if (refreshToken != null && TokenUtils.validateToken(refreshToken)) {
            PrincipalDetails principalDetails = new PrincipalDetails(userMapper.findByUsername(TokenUtils.getUsername(refreshToken)));
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String newToken = TokenUtils.createJwtToken(principalDetails);

            Map<String, String> response = new HashMap<>();
            response.put(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + newToken);
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("refresh token 이 유효하지 않습니다. ");
        }
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

    @GetMapping("/FindIdForm")
    public String showFindIdForm() {
        return "user/FindIdForm";
    }

    @GetMapping("/FindPwForm")
    public String showFindPwForm() {
        return "user/FindPwForm";
    }

    @GetMapping("/my/MyProfile")
    public String showMyProfile() {
        return "user/my/MyProfile";
    }


    @ResponseBody
    @GetMapping("/idCheck")
    public int idCheck(@RequestParam("username") String username) {
        return userService.idCheck(username);
    }

    @ResponseBody
    @GetMapping("/emailCheck")
    public int emailCheck(@RequestParam("email") String userEmail) {
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


    @GetMapping("/Logout")
    public String Logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/"; // 로그인 폼으로 이동
    }


}


