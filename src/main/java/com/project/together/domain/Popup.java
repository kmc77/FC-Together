package com.project.together.domain;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Popup {
    private int id;
    private String popupTitle;
    private String popupContent;
    private String popupImage;
    private LocalDate popupStartDate;
    private LocalDate popupEndDate;
    private String popupType;
    private String createdAt;
    private String updatedAt;
}
