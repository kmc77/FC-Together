package com.project.together.domain;

import lombok.Data;

@Data
public class TrainingSchedule {
    private int scheduleNum; // tsNum -> scheduleNum
    private String username;
    private String scheduleTitle; // tsTitle -> scheduleTitle
    private String scheduleDate; // tsDate -> scheduleDate
    private int scheduleHits; // tsHits -> scheduleHits
    private String scheduleContent; // tsContent -> scheduleContent
    private String tableGb;
}
