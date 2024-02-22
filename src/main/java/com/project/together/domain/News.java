package com.project.together.domain;

import lombok.Data;

@Data
public class News {
    private int newsIdx;
    private String authId;
    private String newsTitle;
    private String newsRegdate;
    private int newsHits;
    private String newsContent;
}
