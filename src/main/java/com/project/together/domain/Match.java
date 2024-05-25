package com.project.together.domain;

import lombok.Data;

import java.util.List;

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
    private String teamLogo;
    private Object obChild;
    private Object obChild2;
    private Object obChild3;
    private List<Team> team;
}