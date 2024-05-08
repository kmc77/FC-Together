package com.project.together.service;

import java.io.IOException;
import java.net.URL;

import com.amazonaws.services.s3.model.ObjectMetadata;
import com.project.together.domain.Team;
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
import java.util.ArrayList;
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

    //공지사항 첨부파일
    public void uploadNoticeFilesToS3AndSaveMetadata(List<MultipartFile> files, int noticeNum, String directory) {
        System.out.println("파일서비스 noticeNum = " + noticeNum);
        for (MultipartFile file : files) {
            // 파일이 비어있지 않은지 확인
            if (!file.isEmpty()) {
                try {
                    // S3에 업로드할 파일 이름 생성
                    String originalFilename = file.getOriginalFilename();
                    String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
                    String filePath = directory + "/" + "noticeNum - " + noticeNum + "/" + fileName;

                    System.out.println("파일 서비스 ==== filePath = " + filePath);
                    // S3에 파일 업로드
                    amazonS3.putObject(new PutObjectRequest(bucketName, filePath, file.getInputStream(), null)
                            .withCannedAcl(CannedAccessControlList.PublicRead));

                    // 업로드된 파일의 URL 생성
                    String fileUrl = amazonS3.getUrl(bucketName, filePath).toString();

                    // 업로드된 파일의 메타데이터를 DB에 저장
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

    // 공지사항 파일을 삭제하는 메서드
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


    // 선수 사진 업로드
    public String uploadPlayerPhotoToS3AndSaveMetadata(MultipartFile file, String selectedPlayerType, String playerNum) {
        String fileUrl = "";
        if (file != null && !file.isEmpty()) {
            try {
                // S3에 업로드할 파일 이름 생성
                String originalFilename = file.getOriginalFilename();
                String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
                // 수정된 부분: 'selectedPlayerType'과 'playerNum'의 순서를 정확히 지정
                String filePath = selectedPlayerType + "- PlayerImg" + "/playerNum - " + playerNum + "/" + fileName;

                System.out.println("선수 사진 업로드 ==== filePath = " + filePath);
                // S3에 파일 업로드
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(file.getSize());
                metadata.setContentType(file.getContentType());
                amazonS3.putObject(new PutObjectRequest(bucketName, filePath, file.getInputStream(), metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));

                // 업로드된 파일의 URL 생성
                fileUrl = amazonS3.getUrl(bucketName, filePath).toString();

                // 업로드된 파일의 메타데이터를 DB에 저장
                File photoMetadata = new File();
                photoMetadata.setFilePath(fileUrl);
                photoMetadata.setTableIdx(Integer.parseInt(playerNum));
                photoMetadata.setFileName(originalFilename);
                photoMetadata.setTableGb(selectedPlayerType + "Player"); // 'selectedPlayerType'을 'table_gb'에 저장
                // 'file' 테이블에 메타데이터 저장
                adminMapper.insertPlayerFile(photoMetadata);
            } catch (Exception e) {
                throw new RuntimeException("선수 사진 업로드 실패: " + e.getMessage());
            }
        }
        return fileUrl;
    }

    // 선수 파일을 삭제하는 메서드(선수 정보 업데이트 시)
    public void deleteFilesByPlayerNumAndType(int playerNum, String selectedPlayerType) {
        // 선수 유형에 따라 처리할 파일 목록 조회
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

        // 선수 유형과 번호에 따라 데이터베이스에서 해당 파일 레코드 삭제
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

    // 선수 정보 다중 삭제 시
    public void deleteS3FilesByPlayerNumsAndType(List<Integer> playerNums, String playerType) {
        // 모든 playerNums에 대해 조회된 파일들을 저장할 리스트
        List<File> allFilesToDelete = new ArrayList<>();

        // 각 playerNum에 대해 파일 정보 조회 및 리스트에 추가
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

        // S3에서 조회된 모든 파일 삭제
        for (File file : allFilesToDelete) {
            String fileKey = getFileKeyFromUrl(file.getFilePath());
            if (fileKey != null && !fileKey.isEmpty()) {
                amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileKey));
            }
        }

        // 데이터베이스에서 해당 파일 레코드 삭제
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

    // 스태프 사진 업로드
    public String uploadStaffPhotoToS3AndSaveMetadata(MultipartFile file, String teamLeagueGb, String teamStaffNum) {
        String fileUrl = "";
        if (file != null && !file.isEmpty()) {
            try {
                // S3에 업로드할 파일 이름 생성
                String originalFilename = file.getOriginalFilename();
                String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
                String filePath = teamLeagueGb + "- StaffImg" + "/staffNum - " + teamStaffNum + "/" + fileName;

                System.out.println("스태프 사진 업로드 ==== filePath = " + filePath);
                // S3에 파일 업로드
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(file.getSize());
                metadata.setContentType(file.getContentType());
                amazonS3.putObject(new PutObjectRequest(bucketName, filePath, file.getInputStream(), metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));

                // 업로드된 파일의 URL 생성
                fileUrl = amazonS3.getUrl(bucketName, filePath).toString();

                // 업로드된 파일의 메타데이터를 DB에 저장
                File photoMetadata = new File();
                photoMetadata.setFilePath(fileUrl);
                photoMetadata.setTableIdx(Integer.parseInt(teamStaffNum));
                photoMetadata.setFileName(originalFilename);
                photoMetadata.setTableGb("TeamStaff");
                // 'file' 테이블에 메타데이터 저장
                adminMapper.insertTeamStaffFile(photoMetadata);
            } catch (Exception e) {
                throw new RuntimeException("선수 사진 업로드 실패: " + e.getMessage());
            }
        }
        return fileUrl;
    }


    // 스태프 파일 삭제
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

    // 스태프 파일 다중 삭제
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


    /**
     * 사용자가 업로드한 팀 로고를 S3에 업로드하고 메타데이터를 저장합니다.
     *
     * @param teamLogo 사용자가 업로드한 MultipartFile.
     * @param teamLeagueGb 리그 식별자.
     * @param teamName 파일 경로와 메타데이터에 사용될 팀 이름.
     * @return 업로드된 파일의 URL.
     */
    public String uploadTeamLogoToS3(MultipartFile teamLogo, String teamLeagueGb, String teamName) {
        String fileUrl = "";
        if (teamLogo != null && !teamLogo.isEmpty()) {
            try {
                String originalFilename = teamLogo.getOriginalFilename();
                String fileName = UUID.randomUUID().toString() + "_" + originalFilename;
                String filePath = teamLeagueGb + "_team" + "/teamLogos/" + teamName + "/" + fileName;

                System.out.println("팀 로고 업로드 ==== filePath = " + filePath);
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(teamLogo.getSize());
                metadata.setContentType(teamLogo.getContentType());
                amazonS3.putObject(new PutObjectRequest(bucketName, filePath, teamLogo.getInputStream(), metadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));

                fileUrl = amazonS3.getUrl(bucketName, filePath).toString();

               /* File logoMetadata = new File();
                logoMetadata.setFilePath(fileUrl);
                *//*logoMetadata.setTableIdx(Integer.parseInt(teamName));*//*  // 필요한 경우 teamName을 추가 정보로 저장
                logoMetadata.setFileName(originalFilename);
                logoMetadata.setTableGb("Team");
                adminMapper.insertTeamFile(logoMetadata);*/

            } catch (IOException e) {
                throw new RuntimeException("팀 로고 업로드 실패: " + e.getMessage());
            }
        }
        return fileUrl;
    }

    // 구단 삭제
    public void deleteFilesByTeamNum(Integer teamId) {
        Team teamName = adminMapper.findTeamById(teamId);
        if (teamName == null) {
            throw new IllegalArgumentException("팀을 찾을 수 없습니다.");
        }

        List<Team> filesToDelete = adminMapper.deleteTeamByTeamId(teamId);

        for (Team team : filesToDelete) {
            String fileKey = getFileKeyFromUrl(team.getTeamLogo());
            if (fileKey != null && !fileKey.isEmpty()) {
                amazonS3.deleteObject(new DeleteObjectRequest(bucketName, fileKey));
            }
        }
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
