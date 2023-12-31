package com.project.together.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Notice {
    private String noticeTitle;
    private String noticeDate;
    private String noticeHits; // int?
    private String noticeContent;
}
