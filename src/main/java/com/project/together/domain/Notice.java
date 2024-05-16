package com.project.together.domain;

import lombok.Data;


@Data
public class Notice {
    private int noticeNum;
    private String username;
    private String noticeTitle;
    private String noticeDate;
    private int noticeHits;
    private String noticeContent;
    private String tableGb;
}
