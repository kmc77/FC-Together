package com.project.together.domain;

import lombok.Data;

@Data
public class Notice {
    private int noticeNum;
    private String authId;
    private String noticeTitle;
    private String noticeDate;
    private int noticeHits;
    private String noticeContent;
}
