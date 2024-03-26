package com.project.together.controller;

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

    @GetMapping("/management_customer_support")
    public String getRuleList(@RequestParam(defaultValue = "0") int start, @RequestParam(defaultValue = "8") int limit, Model model) {
        Map<String, Integer> params = new HashMap<>();
        params.put("start", start);
        params.put("limit", limit);
        System.out.println("======== params = " + params);
        List<Rule> ruleList = managementService.findAll(params);
        System.out.println("========= ruleList = " + ruleList);
        model.addAttribute("ruleList", ruleList);

        return "layout/management/management_customer_support";
    }

    // 상세보기 페이지
    @GetMapping("/management_customer_support_view")
    public String supportViewPage(@RequestParam("no") int ruleNum, Model model) throws NotFoundException {
        Rule rule = managementService.supportViewPage(ruleNum);
        if (rule == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rule not found with id: " + ruleNum);
        }
        model.addAttribute("rule", rule);

        // 이전글 불러오기
        if (ruleNum > 1) {
            Optional<Rule> prevRule = Optional.ofNullable(managementService.supportViewPage(ruleNum - 1));
            prevRule.ifPresent(r -> model.addAttribute("prevRule", r));
        }

        return "layout/management/management_customer_support_view";
    }


/*
    @GetMapping("/management_customer_support_view")
    public String supportViewPage(@RequestParam("no") int ruleNum, Model model) throws NotFoundException {
        Rule rule = managementService.supportViewPage(ruleNum);
        if (rule == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rule not found with id: " + ruleNum);
        }
        model.addAttribute("rule", rule);

        // 이전글 불러오기: 현재 글의 날짜를 기준으로
        Rule prevRule = managementService.findPrevRuleByCurrentRuleDate();
        if (prevRule != null) {
            model.addAttribute("prevRule", prevRule);
        }

        return "layout/management/management_customer_support_view";
    }
*/


}
