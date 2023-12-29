package com.project.together.domain.news;

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
