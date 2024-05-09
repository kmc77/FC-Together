package com.project.together.domain;

import lombok.Data;

@Data
public class Match {
    private int id;
    private String teamName;
    private String matchScore;
    private String matchRelativeScore;
    private String matchDate;
    private String matchTime;
    private String matchLocation;
    private String matchStatus;
    private String matchHome;
    private String leagueGb;
}