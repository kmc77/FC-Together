package com.project.together.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Auth {
    private String authId;
    private String authName;
    private String authPw;
    private String authAdmin;
    private String authManager;
}
