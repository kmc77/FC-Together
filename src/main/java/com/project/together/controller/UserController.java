package com.project.together.controller;

import com.project.together.config.auth.PrincipalDetails;
import com.project.together.config.jwt.JwtProperties;
import com.project.together.config.jwt.TokenUtils;
import com.project.together.domain.*;
import com.project.together.mapper.UserMapper;
import com.project.together.service.UserService;
import com.sun.security.auth.UserPrincipal;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

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

    // 사용자 정보 조회
    @GetMapping("/user")
    public ResponseEntity<User> getUserInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        User user = userService.getFullUserInfo(principalDetails.getUser().getUsername());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 사용자 정보가 없는 경우 404 에러 반환
        }

        return ResponseEntity.ok(user); // User 객체를 직접 반환
    }


    // 토큰 값을 쿠키와 로컬 스토리지에서 가져오는 로직
    @GetMapping("/tokenAll")
    @ResponseBody
    public Map<String, String> tokenAll(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Map<String, String> response = new HashMap<>();
        response.put("username", principalDetails.getUsername());

        System.out.println("사용자 정보 response = " + response);
        return response;
    }

    // 토큰 갱신
    @PostMapping("/refresh_token")
    public ResponseEntity<?> refreshToken(@CookieValue(value = "refreshToken", required = false) String refreshToken) {
        System.out.println("컨트롤러 refreshToken = " + refreshToken);

        if (TokenUtils.validateToken(refreshToken)) {
            String username = TokenUtils.getUsername(refreshToken);
            PrincipalDetails principalDetails = new PrincipalDetails(userMapper.findByUsername(username));
            String newToken = TokenUtils.createJwtToken(principalDetails);

            Map<String, String> response = new HashMap<>();
            response.put("accessToken", newToken); // 클라이언트에서 읽을 수 있도록 "accessToken" 키에 새 토큰을 설정
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("refresh token 이 유효하지 않습니다. ");
        }
    }


    // 어드민 페이지
    @GetMapping("/admin")
    public @ResponseBody String admin() {
        return "어드민 페이지입니다.";
    }

    // 매니저 페이지
    @Secured("ROLE_MANAGER")
    @GetMapping("/manager")
    public @ResponseBody String manager() {
        return "매니저 페이지입니다.";
    }

    // 로그인 폼 페이지
    @GetMapping("/loginform")
    public String loginFormPage() {
        return "user/loginform";
    }

    // 회원 가입 폼 페이지
    @GetMapping("/joinform")
    public String joinFormPage() {
        return "user/joinform";
    }

    // 아이디 찾기 폼 페이지
    @GetMapping("/findidform")
    public String findIdFormPage() {
        return "user/findidform";
    }

    // 비밀번호 찾기 폼 페이지
    @GetMapping("/findpwform")
    public String findPwFormPage() {
        return "user/findpwform";
    }


    // 아이디 중복 체크
    @ResponseBody
    @GetMapping("/idCheck")
    public int idCheck(@RequestParam("username") String username) {
        return userService.idCheck(username);
    }

    // 이메일 중복 체크
    @ResponseBody
    @GetMapping("/emailCheck")
    public int emailCheck(@RequestParam("email") String userEmail) {
        return userService.emailCheck(userEmail);
    }

    // 선수 목록 조회
    @ResponseBody
    @GetMapping("/players")
    public ResponseEntity<?> getPlayers(@RequestParam("selectLeague") String league) {
        if (league.equals("K5_Player")) {
            List<K5_Player> playerList = userService.getK5Players();
            return ResponseEntity.ok(playerList);
        } else if (league.equals("K7_Player")) {
            List<K7_Player> playerList = userService.getK7Players();
            return ResponseEntity.ok(playerList);
        } else if (league.equals("W1_Player")) {
            List<W1_Player> playerList = userService.getW1Players();
            return ResponseEntity.ok(playerList);
        } else {
            return ResponseEntity.badRequest().body("Invalid league parameter");
        }
    }

    // 회원 가입
    @PostMapping("/join")
    public String joinUser(User user, Model model) {
        userService.joinUser(user);
        System.out.println("회원가입 성공 = " + user);
        model.addAttribute("message", "회원가입이 완료되었습니다. 로그인해주세요."); // 메시지를 모델에 추가
        return "redirect:/user/loginform";
    }

    // 로그아웃
    @GetMapping("/Logout")
    public String Logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
        return "redirect:/";
    }

    // 회원 탈퇴
    @DeleteMapping("/delete")
    public String deleteUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        userService.deleteUser(principalDetails.getUsername()); // 사용자 삭제

        // 로그아웃 처리
        SecurityContextHolder.getContext().setAuthentication(null);
        System.out.println("회원탈퇴 성공");

        return "redirect:/";
    }

    // 아이디 찾기(휴대폰 번호 및 이메일 사용)
    @PostMapping("/findId")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> findId(@RequestParam(required = false) String userInput) {
        Map<String, Object> response = new HashMap<>();
        User user = null;

        System.out.println("====== userInput = " + userInput);

        // 정규 표현식을 사용한 이메일과 휴대폰 번호 검증
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        String phoneRegex = "^\\d{2,3}-\\d{3,4}-\\d{4}$"; // 하이픈이 포함된 휴대폰 번호 검증

        if (Pattern.matches(emailRegex, userInput)) {
            user = userService.findIDByEmail(userInput);
        } else if (Pattern.matches(phoneRegex, userInput)) {
            user = userService.findIDByPhoneNum(userInput);
        }

        if (user != null) {
            response.put("found", true);
            response.put("message", "고객님의 아이디는 " + user.getUsername() + " 입니다.");
        } else {
            response.put("found", false);
            response.put("message", "입력하신 정보로 등록된 아이디를 찾을 수 없습니다.");
        }
        return ResponseEntity.ok(response);
    }


    // 비밀번호 찾기(휴대폰 번호 및 이메일 사용)
    @PostMapping("/findPw")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> findPw(@RequestParam(required = false) String userInput) {
        Map<String, Object> response = new HashMap<>();
        User user = null;

        System.out.println("====== userInput = " + userInput);

        // 정규 표현식을 사용한 이메일과 휴대폰 번호 검증
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        String phoneRegex = "^\\d{2,3}-\\d{3,4}-\\d{4}$"; // 하이픈이 포함된 휴대폰 번호 검증

        if (Pattern.matches(emailRegex, userInput)) {
            user = userService.findPWByEmail(userInput);
        } else if (Pattern.matches(phoneRegex, userInput)) {
            user = userService.findPWByPhoneNum(userInput);
        }

        if (user != null) {
            response.put("found", true);
            /*response.put("message", "고객님의 비밀번호는 " + user.getPassword() + " 입니다.");*/
        } else {
            response.put("found", false);
            response.put("message", "입력하신 정보로 등록된 아이디를 찾을 수 없습니다.");
        }
        return ResponseEntity.ok(response);
    }


}
