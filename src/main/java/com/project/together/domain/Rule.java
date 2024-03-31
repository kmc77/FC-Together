package com.project.together.domain;

import lombok.Data;

@Data
public class Rule {
    private int ruleNum;
    private String username;
    private String ruleTitle;
    private String ruleDate;
    private int ruleHits;
    private String ruleContent;
    private String tableGb;
}
