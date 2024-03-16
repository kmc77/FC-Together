package com.project.together.domain;

import lombok.Data;

@Data
public class W_Player {
    private int wPlayerNum;
    private String filePath; // 테이블 컬럼 순서에 맞춰 추가
    private String wPlayerName;
    private String wPlayerEnName;
    private String wPlayerCapYn;
    private String wPlayerSubCapYn;
    private String wPlayerPosition;
    private String wPlayerHeight;
    private String wPlayerWeight;
    private String wPlayerBirth;
}
