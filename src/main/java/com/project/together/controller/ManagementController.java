package com.project.together.controller;

import com.project.together.domain.File;
import com.project.together.domain.Rule;
import com.project.together.service.ManagementService;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/management")
public class ManagementController {

    private final ManagementService managementService;

    @Autowired
    public ManagementController(ManagementService managementService) {
        this.managementService = managementService;
    }

   /* @GetMapping("/management_customer_support")
    public String getRuleList(@RequestParam(defaultValue = "0") int start, @RequestParam(defaultValue = "8") int limit, Model model) {
        Map<String, Integer> params = new HashMap<>();
        params.put("start", start);
        params.put("limit", limit);
        List<Rule> ruleList = managementService.findAll(params);
        model.addAttribute("ruleList", ruleList);

        return "layout/management/management_customer_support";
    }*/


    @GetMapping("/management_customer_support")
    public String getRuleList(@RequestParam(defaultValue = "0") int start, @RequestParam(defaultValue = "8") int limit, Model model) {
        Map<String, Integer> params = new HashMap<>();
        params.put("start", start);
        params.put("limit", limit);
        List<Rule> ruleList = managementService.findAll(params);

        // 각 Rule 객체의 ID를 키로 하고, 파일 존재 여부(Boolean)를 값으로 하는 Map 생성
        Map<Integer, Boolean> filePresenceMap = new HashMap<>();

        for (Rule rule : ruleList) {
            List<File> filesForRule = managementService.findFilesByRuleNum(rule.getRuleNum());
            // 파일이 존재하면 true, 그렇지 않으면 false
            System.out.println("======= filesForRule = " + filesForRule);
            filePresenceMap.put(rule.getRuleNum(), !filesForRule.isEmpty());
        }

        model.addAttribute("ruleList", ruleList);
        model.addAttribute("filePresenceMap", filePresenceMap);
        return "layout/management/management_customer_support";
    }





    // 규정 상세보기 페이지
    @GetMapping("/management_customer_support_view")
    public String supportViewPage(@RequestParam("no") int ruleNum, Model model) throws NotFoundException {
        managementService.increaseRuleHits(ruleNum);
        Rule rule = managementService.supportViewPage(ruleNum);
        if (rule == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rule not found with id: " + ruleNum);
        }

        // 규정 번호에 해당하는 파일 목록 조회
        List<File> files = managementService.findFilesByRuleNum(ruleNum);

        // 모델에 규정 상세 정보와 파일 목록 추가
        model.addAttribute("rule", rule);
        model.addAttribute("files", files); // 파일 목록을 모델에 추가

        // 현재 규정의 날짜를 기준으로 이전 규정을 찾습니다.
        LocalDate currentRuleDate = LocalDate.parse(rule.getRuleDate());
        Rule prevRule = managementService.findPrevRuleByCurrentRuleDate(currentRuleDate);
        if (prevRule != null) {
            model.addAttribute("prevRule", prevRule);
        }

        return "layout/management/management_customer_support_view";
    }




}
