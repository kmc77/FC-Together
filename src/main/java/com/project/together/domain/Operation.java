package com.project.together.domain;

import lombok.Data;

@Data
public class Operation {
    private int operationNum;
    private String username;
    private String operationTitle;
    private String operationDate;
    private int operationHits;
    private String operationContent;
    private String tableGb;
}
