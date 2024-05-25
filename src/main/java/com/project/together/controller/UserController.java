package com.project.together.controller;


import com.project.together.config.auth.PrincipalDetails;
import com.project.together.config.jwt.TokenUtils;
import com.project.together.domain.*;
import com.project.together.mapper.UserMapper;
import com.project.together.service.MailService;
import com.project.together.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final MailService mailService;
    private final UserMapper userMapper;


    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    public UserController(UserService userService, MailService mailService, UserMapper userMapper) {
        this.userService = userService;
        this.mailService = mailService;
        this.userMapper = userMapper;
    }

    // 사용자 정보 조회
    @GetMapping("/user")
    public ResponseEntity<User> getUserInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        if (principalDetails == null || principalDetails.getUser() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = principalDetails.getUser();
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        // 소셜 로그인 사용자인지 확인
        if (user.getProvider() != null && user.getProviderId() != null) {
            User socialUser = User.builder()
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .roles(user.getRoles())
                    .user_real_name(user.getUserRealName())
                    .user_phone(user.getUserPhone())
                    .user_birth(user.getUserBirth())
                    .build();
            return ResponseEntity.ok(socialUser);
        }

        // 일반 로그인 사용자 정보 반환
        user = userService.getFullUserInfo(user.getUsername());

        System.out.println("컨 ====== user = " + user);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 사용자 정보가 없는 경우 404 에러 반환
        }

        return ResponseEntity.ok(user);
    }


    // 토큰 값을 쿠키와 로컬 스토리지에서 가져오는 로직
    @GetMapping("/tokenAll")
    @ResponseBody
    public Map<String, String> tokenAll(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        Map<String, String> response = new HashMap<>();
        response.put("userRealName", principalDetails.getUserRealName());

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
    public String loginFormPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        String authMessage = (String) session.getAttribute("authMessage");
        if (authMessage != null) {
            model.addAttribute("authMessage", authMessage);
            session.removeAttribute("authMessage");
        }
        return "user/loginform"; // user/loginform.html로 렌더링
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
        return "redirect:/user/loginform?success=true";
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
    @ResponseStatus(HttpStatus.SEE_OTHER)
    @DeleteMapping("/delete")
    public String deleteUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("컨 ======= principalDetails.getUsername() = " + principalDetails.getUsername());
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
        List<User> users = new ArrayList<>();

        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        String phoneRegex = "^\\d{2,3}-\\d{3,4}-\\d{4}$";

        if (Pattern.matches(emailRegex, userInput)) {
            users = userService.findUsersByEmail(userInput);
            if (users.size() == 1) {
                response.put("found", true);
                response.put("message", "고객님의 아이디는 " + users.get(0).getUsername() + " 입니다.");
            }
        } else if (Pattern.matches(phoneRegex, userInput)) {
            users = userService.findUsersByPhoneNumber(userInput);
            if (users.size() == 1) {
                response.put("found", true);
                response.put("message", "고객님의 아이디는 " + users.get(0).getUsername() + " 입니다.");
            } else if (users.size() > 1) {
                response.put("found", false);
                response.put("message", "여러 계정이 발견되었습니다. 이메일을 사용하여 아이디를 찾으세요.");
            }
        }

        if (users.isEmpty()) {
            response.put("found", false);
            response.put("message", "입력하신 정보로 등록된 아이디를 찾을 수 없습니다.");
        }
        return ResponseEntity.ok(response);
    }



    // 비밀번호 찾기(아이디와 이메일 사용)
    @PostMapping("/findPw")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> findPw(@RequestParam(required = false) String username, @RequestParam(required = false) String email) {
        Map<String, Object> response = new HashMap<>();
        User user = userService.findByUsernameAndEmail(username, email);

        if (user != null) {
            // 계정을 찾았지만, 인증번호는 아직 보내지 않았음을 명시
            response.put("found", true);
            response.put("message", "계정을 찾았습니다. 이메일로 인증번호를 보내기 위해 '이메일 전송' 버튼을 클릭해주세요.");
        } else {
            response.put("found", false);
            response.put("message", "입력하신 정보로 등록된 계정을 찾을 수 없습니다.");
        }

        return ResponseEntity.ok(response);
    }


    // 비밀번호 재설정 인증번호 메일 발송
    @PostMapping("/sendVerificationCode")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> sendVerificationCode(@RequestParam String username, @RequestParam String email) {
        Map<String, Object> response = new HashMap<>();

        try {
            // 인증번호 생성 및 토큰 발급
            String tokenResponse = TokenUtils.createVerificationToken(username, email);

            // 토큰에서 인증번호 추출
            int verificationCode = TokenUtils.extractVerificationCode(tokenResponse);

            // 메일 서비스를 이용하여 인증 메일 발송
            mailService.sendVerificationMail(email, verificationCode);

            response.put("verificationToken", tokenResponse);
            response.put("success", true);
            response.put("message", "인증번호가 성공적으로 발송되었습니다.");
        } catch (Exception e) { // 메일 발송 실패 및 기타 예외 처리
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "이메일 발송 중 오류가 발생했습니다: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }


    // 비밀번호 재설정 인증 토큰 검증
    @PostMapping("/verifyCode")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> verifyCode(@RequestParam String verificationCode, @RequestParam String verificationToken) {
        Map<String, Object> response = new HashMap<>();

        try {
            // verificationToken을 검증하는 로직
            boolean isValid = TokenUtils.verifyToken(verificationToken, Integer.parseInt(verificationCode));

            if (isValid) {
                response.put("success", true);
                response.put("message", "인증번호가 확인되었습니다.");
            } else {
                response.put("success", false);
                response.put("message", "잘못된 인증번호입니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("success", false);
            response.put("message", "인증번호 검증 중 오류가 발생했습니다: " + e.getMessage());
        }

        return ResponseEntity.ok(response);
    }


    // 비밀번호 재설정
    @PostMapping("/resetPassword")
    public ResponseEntity<Map<String, Object>> resetPassword(@RequestParam("password") String password, @RequestParam("verificationToken") String token) {
        Map<String, Object> response = new HashMap<>();
        if (!TokenUtils.validateToken(token)) {
            response.put("success", false);
            response.put("message", "유효하지 않은 토큰입니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);  // 수정
        }

        User user = userService.getUserByResetPasswordToken(token);
        if (user == null) {
            response.put("success", false);
            response.put("message", "사용자를 찾을 수 없습니다.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);  // 수정
        }

        userService.updatePassword(user, password);
        response.put("success", true);
        response.put("message", "비밀번호가 성공적으로 변경되었습니다.");
        return ResponseEntity.ok(response);  // 확인
    }


}

