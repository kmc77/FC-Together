package com.project.together.domain;

import lombok.Data;

@Data
public class MemberLoginRequest {
    private String member_id;
    private String member_pw;
}
