package com.project.together.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Qna {
    private String authId;
    private String qnaTitle;
    private String qnaContent;
    private String qnaDate;
    private String qnaUpdate;
    private String qnaStatus;
}
