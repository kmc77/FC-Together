package com.project.together.domain;

public class TableData {

    private String authId;
    private String authName;
    private String authPw;
    private String authAdmin;
    private String authManager;

    // 생성자, Getter 및 Setter 메서드는 필요에 따라 추가할 수 있습니다.

    // 예시로 Getter와 Setter 메서드 추가
    public String getAuthId() {
        return authId;
    }

    public void setAuthId(String authId) {
        this.authId = authId;
    }

    public String getAuthName() {
        return authName;
    }

    public void setAuthName(String authName) {
        this.authName = authName;
    }

    public String getAuthPw() {
        return authPw;
    }

    public void setAuthPw(String authPw) {
        this.authPw = authPw;
    }

    public String getAuthAdmin() {
        return authAdmin;
    }

    public void setAuthAdmin(String authAdmin) {
        this.authAdmin = authAdmin;
    }

    public String getAuthManager() {
        return authManager;
    }

    public void setAuthManager(String authManager) {
        this.authManager = authManager;
    }
}

