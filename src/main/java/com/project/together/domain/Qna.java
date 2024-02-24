package com.project.together.domain;

import lombok.Data;

@Data
public class Qna {
    private int qnaNum;
    private int id;
    private String username;
    private String qnaTitle;
    private String qnaContent;
    private String qnaDate;
    private String qnaUpdate;
    private String qnaStatus;
    private Integer qnaView;
}
