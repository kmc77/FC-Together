package com.project.together.service;

import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.project.together.config.S3Config;
import com.project.together.config.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class ImageService {


    private S3Config s3Config;

    @Autowired
    public ImageService(S3Config s3Config) {

        this.s3Config = s3Config;
    }

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    // 사용자의 홈 디렉토리 하위에 myAppImages 디렉토리를 지정
    private String localLocation = "/Users/choegyeongmin/Desktop/Together_imageUpload/";


    public String imageUpload(MultipartRequest request) throws IOException {

        MultipartFile file = request.getFile("upload");

        String fileName = file.getOriginalFilename();
        String ext = fileName.substring(fileName.indexOf("."));

        String uuidFileName = UUID.randomUUID() + ext;
        String localPath = localLocation + uuidFileName;

        File localFile = new File(localPath);
        file.transferTo(localFile);

        s3Config.amazonS3Client().putObject(new PutObjectRequest(bucket, uuidFileName, localFile).withCannedAcl(CannedAccessControlList.PublicRead));
        String s3Url = s3Config.amazonS3Client().getUrl(bucket, uuidFileName).toString();

        localFile.delete();

        return s3Url;

    }
}
