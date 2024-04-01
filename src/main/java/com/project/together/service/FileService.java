package com.project.together.service;

import java.net.MalformedURLException;
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
import com.amazonaws.services.s3.model.DeleteObjectRequest; // Import statement 추가


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
                    String filePath = directory + "/" + fileName;

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
                    adminMapper.insertFile(fileMetadata);
                } catch (Exception e) {
                    throw new RuntimeException("File upload failed: " + e.getMessage());
                }
            }
        }
    }

    public void deleteFilesByRuleNum(int ruleNum) {
        // 규칙 번호에 해당하는 파일 메타데이터 조회
        List<File> filesToDelete = adminMapper.findFilesByRuleNum(ruleNum);

        // S3에서 파일 삭제
        for (File file : filesToDelete) {
            String fileKey = getFileKeyFromUrl(file.getFilePath());
            if (fileKey != null && !fileKey.isEmpty()) {
                // DeleteObjectRequest를 사용하여 파일 삭제 요청
                amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileKey));
            }
        }

        // 데이터베이스에서 파일 메타데이터 삭제
        adminMapper.deleteFilesByRuleNum(ruleNum);
    }

    private String getFileKeyFromUrl(String fileUrl) {
        // URL에서 파일 키 추출 로직 구현
        // 예: https://your-bucket-name.s3.amazonaws.com/ruleFiles/1/uuid_filename.ext
        // 위 URL에서 'ruleFiles/1/uuid_filename.ext' 부분을 추출
        try {
            URL url = new URL(fileUrl);
            String path = url.getPath();
            // S3 객체 키는 첫 슬래시를 제외한 나머지 경로
            return path.substring(1);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
