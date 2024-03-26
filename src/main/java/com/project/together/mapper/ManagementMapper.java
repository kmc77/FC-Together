package com.project.together.mapper;

import com.project.together.domain.Rule;
import org.apache.ibatis.annotations.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface ManagementMapper {
    List<Rule> findAll(Map<String, Integer> params);

    Rule findRuleByRuleNum(int ruleNum);


}
