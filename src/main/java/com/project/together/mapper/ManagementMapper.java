package com.project.together.mapper;

import com.project.together.domain.File;
import com.project.together.domain.Rule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface ManagementMapper {
    List<Rule> findAll(Map<String, Integer> params);

    Rule findRuleByRuleNum(int ruleNum);

    Rule findPrevRuleByCurrentRuleDate(LocalDate currentRuleDate);

    void updateRuleHits(@Param("ruleNum") int ruleNum, @Param("hits") int hits);

    List<File> findFilesByRuleNum(int ruleNum);

}
