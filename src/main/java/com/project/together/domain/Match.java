package com.project.together.domain;

import lombok.Data;

@Data
public class Match {
    private String teamStaffName;
    private String matchResult;
    private String matchScore;
    private String matchDate;
    private String matchTime;
    private String matchLocation;
    private String matchStatus;
    private String matchHome;
}
