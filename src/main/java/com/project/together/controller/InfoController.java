package com.project.together.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/info")
public class InfoController {


    // 1:1 문의하기 페이지
    @GetMapping("qna")
    public String qnaPage() {
        return "info/qna"; // 뷰 이름 반환 (qnaPage.html 또는 qnaPage.jsp 등)
    }

    // 나의 정보 수정 페이지
    @GetMapping("myinfo")
    public String myinfoPage() {
        return "inof/myinfo"; // 뷰 이름 반환 (myinfoPage.html 또는 myinfoPage.jsp 등)
    }

}
