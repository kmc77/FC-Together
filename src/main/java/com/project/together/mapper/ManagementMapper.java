package com.project.together.mapper;

import com.project.together.domain.Faq;
import com.project.together.domain.File;
import com.project.together.domain.Operation;
import com.project.together.domain.Rule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface ManagementMapper {

    /* 임시 */
    List<Rule> findAllRules(Map<String, Integer> params);

    List<Rule> getRuleData();

    List<Rule> findRules(@Param("offset") int offset, @Param("size") int size);

    Rule findRuleByRuleNum(int ruleNum);

    Rule findPrevRuleByCurrentRuleDate(LocalDate currentRuleDate);

    void updateRuleHits(@Param("ruleNum") int ruleNum, @Param("ruleHits") int ruleHits);

    List<File> findFilesByRuleNum(int ruleNum);

    int countRules();

    // ================================== Rule End


    // ================================== 경영공시 start

    List<Operation> findAllOperations(Map<String, Integer> params);

    List<Operation> findOperations(@Param("offset") int offset, @Param("size") int size);

    Operation findOperationByRuleNum(int operationNum);


    Operation findPrevOperationByCurrentOperationDate(LocalDate currentOperationDate);

    void updateOperationHits(@Param("operationNum") int operationNum, @Param("operationHits") int operationHits);

    List<File> findFilesByOperationNum(int operationNum);

    int countOperations();



    // ================================== 경영공시 End

    // ================================== Faq start

    List<Faq> getAllFaqData();


    // ================================== Faq End




}
