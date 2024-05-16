package com.project.together.domain;

import lombok.Data;

@Data
public class ClubVideo {
    private int cvIdx;
    private String username;
    private String cvTitle;
    private String cvContent;
    private int cvHits;
    private String cvDate;
    private String tableGb;
    private String mvTheOriginUrl;
}
