package com.project.together.service;

import java.net.URL;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.project.together.mapper.AdminMapper;
import com.project.together.domain.File;
import com.amazonaws.services.s3.model.DeleteObjectRequest;


import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;


@Service
public class FileService {

    private final AmazonS3 amazonS3;
    private final AdminMapper adminMapper;

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Autowired
    public FileService(AmazonS3 amazonS3, AdminMapper adminMapper) {
        this.amazonS3 = amazonS3;
        this.adminMapper = adminMapper;
    }

    public void uploadFilesToS3AndSaveMetadata(List<MultipartFile> files, int ruleNum, String directory) {
        for (MultipartFile file : files) {
            // 파일이 비어있지 않은지 확인
            if (!file.isEmpty()) {
                try {
                    // S3에 업로드할 파일 이름 생성
                    String originalFilename = file.getOriginalFilename();
                    String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
                    String filePath = directory + "/" + "ruleNum - " + ruleNum + "/" + fileName;

                    System.out.println("파일 서비스 ==== filePath = " + filePath);
                    // S3에 파일 업로드
                    amazonS3.putObject(new PutObjectRequest(bucketName, filePath, file.getInputStream(), null)
                            .withCannedAcl(CannedAccessControlList.PublicRead));

                    // 업로드된 파일의 URL 생성
                    String fileUrl = amazonS3.getUrl(bucketName, filePath).toString();

                    // 업로드된 파일의 메타데이터를 DB에 저장
                    File fileMetadata = new File();
                    fileMetadata.setFilePath(fileUrl);
                    fileMetadata.setTableIdx(ruleNum);
                    fileMetadata.setFileName(originalFilename);
                    fileMetadata.setTableGb("Rule");
                    adminMapper.insertRuleFile(fileMetadata);
                } catch (Exception e) {
                    throw new RuntimeException("File upload failed: " + e.getMessage());
                }
            }
        }
    }

    public void deleteFilesByRuleNum(int ruleNum) {
        List<File> filesToDelete = adminMapper.findFilesByRuleNum(ruleNum);

        for (File file : filesToDelete) {
            String fileKey = getFileKeyFromUrl(file.getFilePath());
            if (fileKey != null && !fileKey.isEmpty()) {
                amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileKey));
            }
        }
        adminMapper.deleteFilesByRuleNum(ruleNum);
    }


    public void uploadOperationFilesToS3AndSaveMetadata(List<MultipartFile> files, int operationNum, String directory) {
        for (MultipartFile file : files) {
            // 파일이 비어있지 않은지 확인
            if (!file.isEmpty()) {
                try {
                    // S3에 업로드할 파일 이름 생성
                    String originalFilename = file.getOriginalFilename();
                    String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
                    String filePath = directory + "/" + "operationNum - " + operationNum + "/" + fileName;

                    System.out.println("파일 서비스 ==== filePath = " + filePath);
                    // S3에 파일 업로드
                    amazonS3.putObject(new PutObjectRequest(bucketName, filePath, file.getInputStream(), null)
                            .withCannedAcl(CannedAccessControlList.PublicRead));

                    // 업로드된 파일의 URL 생성
                    String fileUrl = amazonS3.getUrl(bucketName, filePath).toString();

                    // 업로드된 파일의 메타데이터를 DB에 저장
                    File fileMetadata = new File();
                    fileMetadata.setFilePath(fileUrl);
                    fileMetadata.setTableIdx(operationNum);
                    fileMetadata.setFileName(originalFilename);
                    fileMetadata.setTableGb("Operation");
                    adminMapper.insertOperationFile(fileMetadata);
                } catch (Exception e) {
                    throw new RuntimeException("File upload failed: " + e.getMessage());
                }
            }
        }
    }


    // 경영공시 파일을 삭제하는 메서드
    public void deleteFilesByOperationNum(int operationNum) {
        List<File> filesToDelete = adminMapper.findFilesByOperationNum(operationNum);

        for (File file : filesToDelete) {
            String fileKey = getFileKeyFromUrl(file.getFilePath());
            if (fileKey != null && !fileKey.isEmpty()) {
                amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileKey));
            }
        }
        adminMapper.deleteFilesByOperationNum(operationNum);
    }




    private String getFileKeyFromUrl(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            String path = url.getPath().substring(1); // 첫 번째 '/'를 제외한 나머지 부분
            // URL 디코딩 적용
            return URLDecoder.decode(path, StandardCharsets.UTF_8.toString());
        } catch (Exception e) {
            throw new RuntimeException("Error extracting file key from URL", e);
        }
    }
}
