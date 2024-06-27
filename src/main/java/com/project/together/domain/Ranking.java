package com.project.together.domain;

import lombok.Data;

@Data
public class Ranking {
    private String leagueGb;         // 리그 구분
    private String teamName;         // 팀 이름
    private int gamesPlayed;         // 경기수
    private int wins;                // 승
    private int draws;               // 무
    private int losses;              // 패
    private int points;              // 승점
    private int leagueRank;          // 리그 순위
}
