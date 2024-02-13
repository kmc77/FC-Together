package com.project.together.controller;

import com.project.together.service.InfoService;
import com.project.together.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/info")
public class InfoController {

    private final InfoService infoService;

    @Autowired
    public InfoController(InfoService infoService) {
        this.infoService = infoService;
    }


    // 1:1 문의 작성 처리
    @PostMapping("qna")
    public String writeQna(@RequestParam("qnaTitle") String qnaTitle, @RequestParam("qnaContent") String qnaContent) {
        // qnaTitle과 qnaContent를 사용하여 qna 테이블에 저장하는 로직 구현
        infoService.saveQna(qnaTitle, qnaContent);

        System.out.println("컨트롤러 qnaTitle = " + qnaTitle);
        System.out.println("컨트롤러 qnaContent = " + qnaContent);


        // 저장 후, 리다이렉트할 경로 또는 뷰 이름 반환
        return "redirect:/user/my/myprofile";
    }

}
