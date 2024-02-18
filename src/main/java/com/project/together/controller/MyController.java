package com.project.together.controller;

import com.project.together.config.auth.PrincipalDetails;
import com.project.together.domain.Qna;
import com.project.together.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/my")
public class MyController {

    private final MyService myService;

    @Autowired
    public MyController(MyService myService) {
        this.myService = myService;
    }


    // 1:1 문의 작성 처리
    @PostMapping("qna")
    public String writeQna(@RequestParam("qnaTitle") String qnaTitle, @RequestParam("qnaContent") String qnaContent, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        String username = principalDetails.getUsername();
        int userId = principalDetails.getId();

        System.out.println("컨트롤러 qnaTitle = " + qnaTitle);
        System.out.println("컨트롤러 qnaContent = " + qnaContent);
        System.out.println("컨트롤러 username = " + username);
        System.out.println("컨트롤러 userId = " + userId);

        // qnaTitle과 qnaContent를 사용하여 qna 테이블에 저장하는 로직 구현
        myService.saveQna(qnaTitle, qnaContent, userId, username);

        System.out.println("1:1 문의글 작성 성공");

        // 저장 후, 리다이렉트할 경로 또는 뷰 이름 반환
        return "redirect:/user/my/myprofile";
    }

    //1:1 문의글 목록 조회
    @GetMapping("list")
    public ResponseEntity<List<Qna>> getQnaListForUser(@AuthenticationPrincipal PrincipalDetails principalDetails) {

        String username = principalDetails.getUsername();

        List<Qna> qnaList = myService.getQnaListByUserName(username);

        if (qnaList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(qnaList, HttpStatus.OK);
    }


    // 내 프로필 페이지
    @GetMapping("/info/myprofile")
    public String myProfilePage() {
        return "user/my/info/myprofile";
    }

    //1:1 문의글 상세 페이지
    @GetMapping("/info/qnaview")
    public String qnaViewPage(@RequestParam("no") int qnaNum, Model model) {
        model.addAttribute("qnaNum", qnaNum);
        return "/user/my/info/qnaview";
    }


}
