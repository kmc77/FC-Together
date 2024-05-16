package com.project.together.domain;

import lombok.Data;

@Data
public class File {
    private String filePath;
    private int tableIdx;
    private String fileName;
    private String tableGb;
    private int fileIdx;

}
