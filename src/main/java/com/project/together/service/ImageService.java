package com.project.together.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.project.together.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.IOException;
import java.net.URL;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {
    private final AmazonS3 amazonS3;
    private final AdminMapper adminMapper; // 이미지 URL을 찾기 위해 AdminMapper가 필요함

    @Value("${cloud.aws.s3.bucket}")
    private String bucketName;

    @Autowired
    public ImageService(AmazonS3 amazonS3, AdminMapper adminMapper) {
        this.amazonS3 = amazonS3;
        this.adminMapper = adminMapper;
    }

    public String imageUpload(MultipartRequest request) throws IOException {
        MultipartFile file = request.getFile("upload");

        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + fileExtension;

        amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        String fileUrl = amazonS3.getUrl(bucketName, fileName).toString();

        return fileUrl;
    }

    public String ruleImageUpload(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            // 파일이 없는 경우, 적절한 처리를 하고, null 또는 빈 문자열을 반환합니다.
            return null;
        }
        try {
            // 파일명 인코딩 처리
            String originalFilename = URLEncoder.encode(file.getOriginalFilename(), StandardCharsets.UTF_8.toString());
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));

            // "imageFiles" 디렉토리에 파일 저장
            String filePath = "ruleFiles/" + "imageFiles/" + UUID.randomUUID().toString() + fileExtension;

            // S3에 파일 업로드
            amazonS3.putObject(new PutObjectRequest(bucketName, filePath, file.getInputStream(), null)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            // 업로드된 파일 URL 반환
            String fileUrl = amazonS3.getUrl(bucketName, filePath).toString();

            return fileUrl;
        } catch (IOException e) {
            // IOException 처리
            e.printStackTrace();
            return null;
        }
    }



    public void deleteImageByRuleNum(int ruleNum) {
        // 규정 번호에 해당하는 모든 이미지 URL을 데이터베이스에서 조회
        List<String> imageUrls = adminMapper.findImageUrlsByRuleNum(ruleNum);

        // 조회된 모든 이미지 URL에 대하여 삭제를 수행
        for (String imageUrl : imageUrls) {
            deleteImageByUrl(imageUrl);
        }
    }

    public void deleteImageByUrl(String imageUrl) {
        String fileKey = extractFileKeyFromUrl(imageUrl);
        if (!fileKey.isEmpty()) {
            amazonS3.deleteObject(bucketName, fileKey);
        }
    }

    private String extractFileKeyFromUrl(String url) {
        try {
            URL fileUrl = new URL(url);
            String path = fileUrl.getPath();
            return path.substring(1); // 첫 번째 '/'를 제외한 나머지 부분
        } catch (MalformedURLException e) {
            throw new RuntimeException("Invalid URL format", e);
        }
    }


}
