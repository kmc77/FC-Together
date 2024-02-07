package com.project.together.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/my")
public class MyController {


    @GetMapping("qna")
    public String qnaPage() {
        // 1:1 문의하기 페이지에 대한 로직 또는 처리를 작성
        return "qna"; // 뷰 이름 반환 (qnaPage.html 또는 qnaPage.jsp 등)
    }

    @GetMapping("myprofile")
    public String showMyProfile() {
        return "user/my/myprofile";
    }
}
