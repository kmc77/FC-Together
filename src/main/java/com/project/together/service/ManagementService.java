package com.project.together.service;

import com.project.together.domain.Rule;
import com.project.together.mapper.ManagementMapper;
import com.project.together.mapper.MediaMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ManagementService {

    private final ManagementMapper managementMapper;


    public List<Rule> findAll(Map<String, Integer> params) {
        return managementMapper.findAll(params);
    }

    public Rule supportViewPage(int ruleNum) throws NotFoundException {
        System.out.println("서비스 ruleNum = " + ruleNum);
        Rule rule = managementMapper.findRuleByRuleNum(ruleNum);
        if (rule == null) {
            throw new NotFoundException(ruleNum + "번호의 규정을 찾을 수 없습니다.");
        }
        return rule;
    }


    /*public Rule findPrevRuleByCurrentRuleDate() {

    }*/
}
