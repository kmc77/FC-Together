package com.project.together.domain;

import lombok.Data;
@Data
public class TeamStaff {
    private String teamStaffName;
    private String teamStaffRole;
    private String teamLeagueGb;
    private int teamStaffNum;
    private String teamStaffEnName;
    private String teamStaffBirth;
    private String filePath;  // 파일 경로 필드 추가
}