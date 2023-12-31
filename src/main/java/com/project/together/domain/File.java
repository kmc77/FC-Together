package com.project.together.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class File {
    private String filePath;
    private int tableIdx;
    private String fileName;
    private String tableGb;
    private int fileIdx;
}
