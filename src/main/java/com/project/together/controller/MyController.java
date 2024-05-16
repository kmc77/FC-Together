package com.project.together.controller;

import com.project.together.config.auth.PrincipalDetails;
import com.project.together.domain.Qna;
import com.project.together.service.MyService;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
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
        return "/user/my/myprofile";
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
    @GetMapping("/myprofile")
    public String myProfilePage() {
        return "user/my/myprofile";
    }

    //1:1 문의글 상세 페이지
    @GetMapping("/qnaview")
    public String qnaViewPage(@RequestParam("no") int qnaNum, Model model) throws NotFoundException {
        Qna qna = myService.getQna(qnaNum);  // QnaService를 사용하여 데이터를 가져옴
        model.addAttribute("qna", qna);  // Model에 데이터를 추가
        return "/user/my/qnaview";
    }

    //문의글 삭제
    @DeleteMapping("/qna/{qnaNum}")
    public ResponseEntity<?> deleteQna(@PathVariable("qnaNum") int qnaNum, @AuthenticationPrincipal PrincipalDetails principalDetails) {
        String username = principalDetails.getUsername();

        if (username != null) {
            try {
                myService.deleteQna(username, qnaNum);
                return ResponseEntity.ok().build();
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("삭제할 권한이 없습니다.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("사용자가 인증되지 않았습니다.");
        }
    }


}
