package com.project.together.domain;

import lombok.Data;

@Data
public class Faq {
    private int faqId;
    private String faqCategory;
    private String faqQuestion;
    private String faqAnswer;
}
