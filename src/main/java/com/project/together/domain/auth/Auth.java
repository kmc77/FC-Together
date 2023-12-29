package com.project.together.domain.auth;

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
