package com.project.together.domain;

import lombok.Data;

@Data
public class W1_Player {
    private int w1PlayerNum;
    private String w1filePath; // 테이블 컬럼 순서에 맞춰 추가
    private String w1PlayerName;
    private String w1PlayerEnName;
    private String w1PlayerCapYn;
    private String w1PlayerSubCapYn;
    private String w1PlayerPosition;
    private String w1PlayerHeight;
    private String w1PlayerWeight;
    private String w1PlayerBirth;
}
