package com.project.together.domain;

import lombok.Data;

@Data
public class User {

    private int id;
    private String username;
    private String password;
    private String user_email;
    private String user_role;
    private String user_real_name;
    private String user_gender;
    private String user_phone;
    private String user_birth;
    private String user_address;
    private String user_like_player;
    private String user_like_player_gb;
    private String user_marketing;

}
