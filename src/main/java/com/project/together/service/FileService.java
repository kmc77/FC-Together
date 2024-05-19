package com.project.together.service;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.net.URLEncoder;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.project.together.domain.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.project.together.mapper.AdminMapper;
import com.project.together.domain.File;

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
            if (!file.isEmpty()) {
                try {
                    String originalFilename = file.getOriginalFilename();
                    String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
                    String filePath = directory + "/" + "ruleNum - " + ruleNum + "/" + fileName;

                    amazonS3.putObject(new PutObjectRequest(bucketName, filePath, file.getInputStream(), null)
                            .withCannedAcl(CannedAccessControlList.PublicRead));

                    String fileUrl = amazonS3.getUrl(bucketName, filePath).toString();

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

    public void uploadNoticeFilesToS3AndSaveMetadata(List<MultipartFile> files, int noticeNum, String directory) {
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    String originalFilename = file.getOriginalFilename();
                    String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
                    String filePath = directory + "/" + "noticeNum - " + noticeNum + "/" + fileName;

                    amazonS3.putObject(new PutObjectRequest(bucketName, filePath, file.getInputStream(), null)
                            .withCannedAcl(CannedAccessControlList.PublicRead));

                    String fileUrl = amazonS3.getUrl(bucketName, filePath).toString();

                    File fileMetadata = new File();
                    fileMetadata.setFilePath(fileUrl);
                    fileMetadata.setTableIdx(noticeNum);
                    fileMetadata.setFileName(originalFilename);
                    fileMetadata.setTableGb("Notice");
                    adminMapper.insertNoticeFile(fileMetadata);
                } catch (Exception e) {
                    throw new RuntimeException("File upload failed: " + e.getMessage());
                }
            }
        }
    }

    public void deleteFilesByNoticeNum(int noticeNum) {
        List<File> filesToDelete = adminMapper.findFilesByNoticeNum(noticeNum);

        for (File file : filesToDelete) {
            String fileKey = getFileKeyFromUrl(file.getFilePath());
            if (fileKey != null && !fileKey.isEmpty()) {
                amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileKey));
            }
        }
        adminMapper.deleteFilesByNoticeNum(noticeNum);
    }

    public void uploadOperationFilesToS3AndSaveMetadata(List<MultipartFile> files, int operationNum, String directory) {
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                try {
                    String originalFilename = file.getOriginalFilename();
                    String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
                    String filePath = directory + "/" + "operationNum - " + operationNum + "/" + fileName;

                    amazonS3.putObject(new PutObjectRequest(bucketName, filePath, file.getInputStream(), null)
                            .withCannedAcl(CannedAccessControlList.PublicRead));

                    String fileUrl = amazonS3.getUrl(bucketName, filePath).toString();

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

    public String uploadPlayerPhotoToS3AndSaveMetadata(MultipartFile file, String selectedPlayerType, String playerNum) {
        String fileUrl = "";
        if (file != null && !file.isEmpty()) {
            try {
                String originalFilename = file.getOriginalFilename();
                String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
                String filePath = selectedPlayerType + "_team/" + "PlayerImg" + "/PlayerNum - " + playerNum + "/" + fileName;

                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(file.getSize());
                metadata.setContentType(file.getContentType());
                amazonS3.putObject(new PutObjectRequest(bucketName, filePath, file.getInputStream(), metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));

                fileUrl = amazonS3.getUrl(bucketName, filePath).toString();

                File photoMetadata = new File();
                photoMetadata.setFilePath(fileUrl);
                photoMetadata.setTableIdx(Integer.parseInt(playerNum));
                photoMetadata.setFileName(originalFilename);
                photoMetadata.setTableGb(selectedPlayerType + "Player");
                adminMapper.insertPlayerFile(photoMetadata);
            } catch (Exception e) {
                throw new RuntimeException("선수 사진 업로드 실패: " + e.getMessage());
            }
        }
        return fileUrl;
    }

    public void deleteFilesByPlayerNumAndType(int playerNum, String selectedPlayerType) {
        List<File> filesToDelete;
        switch (selectedPlayerType) {
            case "k5":
                filesToDelete = adminMapper.findFilesByK5PlayerNum(playerNum);
                break;
            case "k7":
                filesToDelete = adminMapper.findFilesByK7PlayerNum(playerNum);
                break;
            case "w1":
                filesToDelete = adminMapper.findFilesByW1PlayerNum(playerNum);
                break;
            default:
                throw new IllegalArgumentException("Unsupported player type: " + selectedPlayerType);
        }

        for (File file : filesToDelete) {
            String fileKey = getFileKeyFromUrl(file.getFilePath());
            if (fileKey != null && !fileKey.isEmpty()) {
                amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileKey));
            }
        }

        switch (selectedPlayerType) {
            case "k5":
                adminMapper.deleteFilesByK5PlayerNum(playerNum);
                break;
            case "k7":
                adminMapper.deleteFilesByK7PlayerNum(playerNum);
                break;
            case "w1":
                adminMapper.deleteFilesByW1PlayerNum(playerNum);
                break;
        }
    }

    public void deleteS3FilesByPlayerNumsAndType(List<Integer> playerNums, String playerType) {
        List<File> allFilesToDelete = new ArrayList<>();

        for (Integer playerNum : playerNums) {
            List<File> filesToDelete;
            switch (playerType) {
                case "k5":
                    filesToDelete = adminMapper.findFilesByK5PlayerNum(playerNum);
                    break;
                case "k7":
                    filesToDelete = adminMapper.findFilesByK7PlayerNum(playerNum);
                    break;
                case "w1":
                    filesToDelete = adminMapper.findFilesByW1PlayerNum(playerNum);
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported player type: " + playerType);
            }
            allFilesToDelete.addAll(filesToDelete);
        }

        for (File file : allFilesToDelete) {
            String fileKey = getFileKeyFromUrl(file.getFilePath());
            if (fileKey != null && !fileKey.isEmpty()) {
                amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileKey));
            }
        }

        for (Integer playerNum : playerNums) {
            switch (playerType) {
                case "k5":
                    adminMapper.deleteFilesByK5PlayerNum(playerNum);
                    break;
                case "k7":
                    adminMapper.deleteFilesByK7PlayerNum(playerNum);
                    break;
                case "w1":
                    adminMapper.deleteFilesByW1PlayerNum(playerNum);
                    break;
            }
        }
    }

    public String uploadStaffPhotoToS3AndSaveMetadata(MultipartFile file, String teamLeagueGb, String teamStaffNum) {
        String fileUrl = "";
        if (file != null && !file.isEmpty()) {
            try {
                String originalFilename = file.getOriginalFilename();
                String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
                String filePath = teamLeagueGb + "_team/" + "StaffImg" + "/StaffNum - " + teamStaffNum + "/" + fileName;

                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(file.getSize());
                metadata.setContentType(file.getContentType());
                amazonS3.putObject(new PutObjectRequest(bucketName, filePath, file.getInputStream(), metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));

                fileUrl = amazonS3.getUrl(bucketName, filePath).toString();

                File photoMetadata = new File();
                photoMetadata.setFilePath(fileUrl);
                photoMetadata.setTableIdx(Integer.parseInt(teamStaffNum));
                photoMetadata.setFileName(originalFilename);
                photoMetadata.setTableGb("TeamStaff");
                adminMapper.insertTeamStaffFile(photoMetadata);
            } catch (Exception e) {
                throw new RuntimeException("스태프 사진 업로드 실패: " + e.getMessage());
            }
        }
        return fileUrl;
    }

    // SectionClubPhoto add
    public void uploadSectionClubPhotoFilesToS3AndSaveMetadata(List<MultipartFile> multipartFiles, String tableGb, String username) {
        for (MultipartFile file : multipartFiles) {
            if (file != null && !file.isEmpty()) {
                try {
                    String originalFilename = file.getOriginalFilename();
                    String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
                    String filePath = "sectionClubPhoto/" + fileName;

                    ObjectMetadata metadata = new ObjectMetadata();
                    metadata.setContentLength(file.getSize());
                    metadata.setContentType(file.getContentType());
                    amazonS3.putObject(new PutObjectRequest(bucketName, filePath, file.getInputStream(), metadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));

                    String fileUrl = amazonS3.getUrl(bucketName, filePath).toString();

                    File photoMetadata = new File();
                    photoMetadata.setFilePath(fileUrl);
                    photoMetadata.setTableIdx(0); // 필요에 따라 수정
                    photoMetadata.setFileName(originalFilename);
                    photoMetadata.setTableGb(tableGb);
                    adminMapper.insertFile(photoMetadata);
                } catch (Exception e) {
                    throw new RuntimeException("클럽 사진 업로드 실패: " + e.getMessage());
                }
            }
        }
    }





    public void deleteFilesByStaffNum(int teamStaffNum) {
        List<File> filesToDelete = adminMapper.findFilesByTeamStaffNum(teamStaffNum);

        for (File file : filesToDelete) {
            String fileKey = getFileKeyFromUrl(file.getFilePath());
            if (fileKey != null && !fileKey.isEmpty()) {
                amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileKey));
            }
        }
        adminMapper.deleteFilesByTeamStaffNum(teamStaffNum);
    }

    public void deleteFilesByStaffNums(List<Integer> teamStaffNum) {
        List<File> filesToDelete = adminMapper.findFilesByTeamStaffNums(teamStaffNum);

        for (File file : filesToDelete) {
            String fileKey = getFileKeyFromUrl(file.getFilePath());
            if (fileKey != null && !fileKey.isEmpty()) {
                amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileKey));
            }
        }
        adminMapper.deleteFilesByTeamStaffNums(teamStaffNum);
    }

    public String uploadTeamLogoToS3(MultipartFile teamLogo, String teamLeagueGb, String teamName) {
        String fileUrl = "";
        if (teamLogo != null && !teamLogo.isEmpty()) {
            try {
                String originalFilename = teamLogo.getOriginalFilename();
                String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
                String filePath = teamLeagueGb + "_team" + "/TeamLogos/" + teamName + "/" + fileName;

                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(teamLogo.getSize());
                metadata.setContentType(teamLogo.getContentType());
                amazonS3.putObject(new PutObjectRequest(bucketName, filePath, teamLogo.getInputStream(), metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));

                fileUrl = amazonS3.getUrl(bucketName, filePath).toString();

            } catch (IOException e) {
                throw new RuntimeException("팀 로고 업로드 실패: " + e.getMessage());
            }
        }
        return fileUrl;
    }

    public void deleteFilesByTeamNum(List<Integer> teamIds) {
        for (Integer teamId : teamIds) {
            Team team = adminMapper.findTeamById(teamId);
            if (team == null) {
                throw new IllegalArgumentException("팀을 찾을 수 없습니다: " + teamId);
            }

            String teamLogoUrl = team.getTeamLogo();
            if (teamLogoUrl == null || teamLogoUrl.isEmpty()) {
                System.err.println("팀 로고 URL이 없습니다: " + teamId);
            } else {
                String fileKey = getFileKeyFromUrl(teamLogoUrl);
                if (fileKey != null && !fileKey.isEmpty()) {
                    amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileKey));
                }
            }
            adminMapper.deleteTeamByTeamId(Collections.singletonList(teamId));
        }
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

    /*public String ruleImageUpload(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            String originalFilename = URLEncoder.encode(file.getOriginalFilename(), StandardCharsets.UTF_8.toString());
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String filePath = "ruleFiles/" + "imageFiles/" + UUID.randomUUID().toString() + fileExtension;

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
    }*/

    public String ruleImageUpload(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            // 파일명 인코딩 처리
            String originalFilename = URLEncoder.encode(file.getOriginalFilename(), StandardCharsets.UTF_8.toString());
            String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileName = UUID.randomUUID().toString() + fileExtension;
            String filePath = "ruleFiles/imageFiles/" + fileName; // 경로 수정

            // S3에 파일 업로드
            amazonS3.putObject(new PutObjectRequest(bucketName, filePath, file.getInputStream(), null)
                    .withCannedAcl(CannedAccessControlList.PublicRead));

            // 업로드된 파일 URL 생성
            String fileUrl = amazonS3.getUrl(bucketName, filePath).toString();

            // 업로드된 파일의 메타데이터를 DB에 저장
            File fileMetadata = new File();
            fileMetadata.setFilePath(fileUrl);
            fileMetadata.setFileName(originalFilename);
            fileMetadata.setTableGb("RuleImage");
            adminMapper.insertRuleFile(fileMetadata); // 규칙 파일 메타데이터 저장

            return fileUrl;
        } catch (IOException e) {
            // IOException 처리
            e.printStackTrace();
            return null;
        }
    }



    /*public void findAndDeleteImagesByFilePath() {
        List<String> imageUrls = adminMapper.findAllImageUrls();
        for (String imageUrl : imageUrls) {
            deleteImageByFilePath(imageUrl);
        }
    }*/

    public void deleteImageByFilePath(String filePath) {
        String fileKey = getFileKeyFromUrl(filePath);
        if (fileKey != null && !fileKey.isEmpty()) {
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileKey));
        }
    }

    public void deleteFilesByFileIdx(Long fileIdx) {
        File fileToDelete = adminMapper.findFileByFileIdx(fileIdx);
        if (fileToDelete != null) {
            String fileKey = getFileKeyFromUrl(fileToDelete.getFilePath());
            if (fileKey != null && !fileKey.isEmpty()) {
                amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileKey));
            }
            adminMapper.deleteFileByFileIdx(fileIdx);
        }
    }


    public void deleteImageByOperationNum(int operationNum) {
        List<String> imageUrls = adminMapper.findImageUrlsByOperationNum(operationNum);
        for (String imageUrl : imageUrls) {
            deleteImageByUrl(imageUrl);
        }
    }

    public void deleteImageByUrl(String imageUrl) {
        String fileKey = getFileKeyFromUrl(imageUrl);
        if (!fileKey.isEmpty()) {
            System.out.println("======== Deleting file with key: " + fileKey);  // 디버그 로그 추가

            amazonS3.deleteObject(bucketName, fileKey);
        }
    }

    private String getFileKeyFromUrl(String fileUrl) {
        try {
            if (fileUrl != null && !fileUrl.isEmpty()) {
                URL url = new URL(fileUrl);
                String path = url.getPath().substring(1);
                return URLDecoder.decode(path, StandardCharsets.UTF_8.toString());
            } else {
                System.err.println("잘못된 또는 빈 파일 URL: " + fileUrl);
                return null;
            }
        } catch (Exception e) {
            System.err.println("파일 URL에서 키 추출 중 오류 발생: " + fileUrl);
            e.printStackTrace();
            return null;
        }
    }


    public List<File> getImagesForSectionClubPhoto() {
        return adminMapper.findFilesByTableGb("sectionClubPhoto");
    }
}
