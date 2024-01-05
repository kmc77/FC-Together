package com.project.together.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class News {
    private int newsIdx;
    private String newsTitle;
    private String newsRegdate;
    private int newsHits;
    private String newsContent;
}
