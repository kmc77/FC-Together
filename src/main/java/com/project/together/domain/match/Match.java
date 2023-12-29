package com.project.together.domain.match;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Match {
    private String teamName;
    private String matchResult;
    private String matchScore;
    private String matchDate;
    private String matchTime;
    private String matchLocation;
    private String matchStatus;
    private String matchHome;
}
