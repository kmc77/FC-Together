package com.project.together.service;

import com.project.together.domain.*;
import com.project.together.mapper.ManagementMapper;
import com.project.together.mapper.MediaMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ManagementService {

    private final ManagementMapper managementMapper;

    /* 임시 */
    public List<Rule> findAllRules(Map<String, Integer> params) {
        return managementMapper.findAllRules(params);
    }

    public List<Rule> getRuleData(int offset, int size) {
        return managementMapper.findRules(offset, size);
    }

    public Rule viewRuleSupportPage(int ruleNum) throws NotFoundException {
        System.out.println("서비스 ruleNum = " + ruleNum);
        Rule rule = managementMapper.findRuleByRuleNum(ruleNum);
        if (rule == null) {
            throw new NotFoundException(ruleNum + "번호의 규정을 찾을 수 없습니다.");
        }
        return rule;
    }

    public int countRules() {
        return managementMapper.countRules();
    }

    public Rule findPrevRuleByCurrentRuleDate(LocalDate currentRuleDate) {
        return managementMapper.findPrevRuleByCurrentRuleDate(currentRuleDate);
    }

    public void increaseRuleHits(int ruleNum) throws NotFoundException {
        Rule rule = managementMapper.findRuleByRuleNum(ruleNum);
        if (rule == null) {
            throw new NotFoundException(ruleNum + "번 규정을 찾을 수 없습니다.");
        }
        rule.setRuleHits(rule.getRuleHits() + 1); // 조회수 1 증가
        managementMapper.updateRuleHits(rule.getRuleNum(), rule.getRuleHits());
    }


    public List<File> findFilesByRuleNum(int ruleNum) {
        return managementMapper.findFilesByRuleNum(ruleNum);
    }


    // ================================== Rule End


    // ================================== 경영공시 start


    public List<Operation> findAllOperations(Map<String, Integer> params) {
        return managementMapper.findAllOperations(params);
    }

    public List<Operation> getOperationData(int offset, int size) {
        return managementMapper.findOperations(offset, size);
    }

    public Operation viewOperationSupportPage(int operationNum) throws NotFoundException {
        System.out.println("서비스 operationNum = " + operationNum);
        Operation operation = managementMapper.findOperationByRuleNum(operationNum);
        if (operation == null) {
            throw new NotFoundException(operationNum + "번호의 공시를 찾을 수 없습니다.");
        }
        return operation;
    }

    public int countOperations() {
        return managementMapper.countOperations();
    }

    public List<File> findFilesByOperationNum(int operationNum) {
        return managementMapper.findFilesByOperationNum(operationNum);
    }

    public void increaseOperationHits(int operationNum) throws NotFoundException {
        Operation operation = managementMapper.findOperationByRuleNum(operationNum);
        if (operation == null) {
            throw new NotFoundException(operationNum + "번 데이터를 찾을 수 없습니다.");
        }
        operation.setOperationHits(operation.getOperationHits() + 1);
        managementMapper.updateOperationHits(operation.getOperationNum(), operation.getOperationHits());
    }

    public Operation findPrevOperationByCurrentOperationDate(LocalDate currentOperationDate) {
        return managementMapper.findPrevOperationByCurrentOperationDate(currentOperationDate);
    }


    // ================================== 경영공시 End

    // ================================== Faq start

    public List<Faq> getAllFaqData() {
        return managementMapper.getAllFaqData();
    }


    // ================================== Faq End





}
