package com.project.together.controller;

import com.project.together.config.auth.PrincipalDetails;
import com.project.together.domain.*;
import com.project.together.service.AdminService;
import com.project.together.service.FileService;
import com.project.together.service.ImageService;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;
    private final FileService fileService;
    private final ImageService imageService;

    public AdminController(AdminService adminService, FileService fileService, ImageService imageService) {
        this.adminService = adminService;
        this.fileService = fileService;
        this.imageService = imageService;
    }

    // 사용자 정보 가져오기
    @GetMapping("/layout/getUserInfo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<PrincipalDetails>> getUserInfo() {
        List<PrincipalDetails> users = adminService.getAllUsers();
        System.out.println("users = " + users);
        return ResponseEntity.ok(users);
    }

    // ================================== QnA start

    // QnA 정보 가져오기
    @GetMapping("/layout/getQnAInfo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Qna>> getQnAInfo() {
        List<Qna> qnas = adminService.getAllQnAs();
        System.out.println("qnas = " + qnas);
        return ResponseEntity.ok(qnas);
    }

    // QnA 상세 정보 가져오기
    @GetMapping("/layout/qnaview")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Qna> getQnaView(@RequestParam("qnaNum") int qnaNum, HttpServletResponse response) {
        Qna qna = adminService.findById(qnaNum);
        System.out.println("qnaview = " + qna);
        if (qna != null) {
            return ResponseEntity.ok(qna);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // QnA 답변 작성
    @PostMapping("/layout/adminReply")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminReply(@RequestParam("qnaNum") String qnaNum,
                             @RequestParam("authId") String authId,
                             @RequestParam("qnaReply") String qnaReply) {

        // QnA 서비스를 이용하여 데이터베이스 업데이트
        boolean isSuccess = adminService.updateQnA(qnaNum, authId, qnaReply);

        return "layout/common/board/qna";
    }

    // QnA 삭제
    @PostMapping("/layout/qnaDelete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> qnaDelete(@RequestParam("qnaNum") List<Integer> qnaNums) {
        try {
            adminService.deleteQna(qnaNums);


            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {

            e.printStackTrace();


            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ================================== QnA End

    // ================================== Notice start

    // 공지사항 정보 가져오기
    @GetMapping("/layout/getNoticeInfo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Notice>> getNoticeInfo() {
        List<Notice> notices = adminService.getAllNotice();
        System.out.println("notices = " + notices);
        return ResponseEntity.ok(notices);
    }

    // 공지사항 상세 정보 가져오기(업로드 파일 추가)
    @GetMapping("/layout/noticeView/{noticeNum}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getNoticeDetails(@PathVariable int noticeNum) throws NotFoundException {
        System.out.println("컨트롤러 noticeNum = " + noticeNum);
        adminService.increaseNoticeHits(noticeNum);
        Notice notice = adminService.findNoticesById(noticeNum);
        if (notice == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<File> files = adminService.findFilesByNoticeNum(noticeNum); // 공지사항 번호에 해당하는 파일 목록 조회

        // 공지사항 정보와 파일 목록을 포함하는 객체 생성
        Map<String, Object> response = new HashMap<>();
        response.put("notice", notice);
        response.put("files", files);

        return ResponseEntity.ok(response);
    }


    // 공지사항 작성
    @PostMapping("/layout/noticePost")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String postNotice(@ModelAttribute Notice notice,
                             @RequestParam String username,
                             @RequestParam("files") List<MultipartFile> files,
                             RedirectAttributes redirectAttributes) {
        notice.setUsername(username);
        int noticeNum = adminService.saveNotice(notice);
        System.out.println("컨트롤러 noticeNum = " + noticeNum);
        if (!files.isEmpty()) {
            // FileService를 이용하여 S3에 파일을 업로드하고 메타데이터를 저장
            fileService.uploadNoticeFilesToS3AndSaveMetadata(files, noticeNum, "noticeFiles");
        }
        redirectAttributes.addFlashAttribute("message", "공지사항이 성공적으로 추가되었습니다.");
        return "redirect:/admin/layout/notice_management";
    }

    // 공지사항 수정
    @PostMapping("/layout/noticeUpdate/{noticeNum}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateNotice(@PathVariable int noticeNum,
                               @ModelAttribute Notice notice,
                               @RequestParam("files") List<MultipartFile> files,
                               RedirectAttributes redirectAttributes) {
        notice.setNoticeNum(noticeNum);
        adminService.updateNotice(notice);

        // 기존 파일 정보 삭제
        fileService.deleteFilesByNoticeNum(noticeNum);

        // 새로운 파일 처리
        if (!files.isEmpty()) {
            fileService.uploadNoticeFilesToS3AndSaveMetadata(files, noticeNum, "noticeFiles");
        }

        redirectAttributes.addFlashAttribute("message", "공지사항이 성공적으로 업데이트되었습니다.");
        return "redirect:/admin/layout/notice_management";
    }



    // 공지사항 삭제
    @PostMapping("/layout/noticeDelete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> noticeDelete(@RequestParam("noticeNum") List<Integer> noticeNums) {
        try {
            // 먼저 각 번호에 해당하는 파일을 S3에서 삭제합니다.
            noticeNums.forEach(fileService::deleteFilesByNoticeNum);
            // 각 번호에 해당하는 이미지 파일을 S3에서 삭제
            /*noticeNums.forEach(imageService::deleteImageByNoticeNum);*/

            adminService.noticeDelete(noticeNums);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* // 공지사항 삭제
    @PostMapping("/layout/noticeDelete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> noticeDelete(@RequestParam("noticeNum") List<Integer> noticeNums) {
        try {
            adminService.noticeDelete(noticeNums);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }*/

    // ================================== Notice End

    // ================================== News start


    // 구단뉴스 정보 가져오기
    @GetMapping("/layout/getNewsInfo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<News>> getNewsInfo() {
        List<News> newsList = adminService.getAllNews();
        System.out.println("newsList = " + newsList);
        return ResponseEntity.ok(newsList);
    }


    // 구단소식 상세 정보 가져오기
    @GetMapping("/layout/newsView")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<News> getNewsView(@RequestParam("newsIdx") int newsIdx, HttpServletResponse response) {
        News news = adminService.findNewsById(newsIdx);
        System.out.println("newsView = " + news);
        if (news != null) {
            return ResponseEntity.ok(news);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 구단소식 작성
    @PostMapping("/layout/newsPost")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String postNews(@ModelAttribute News news, @RequestParam String username, RedirectAttributes redirectAttributes) {
        news.setUsername(username);
        adminService.saveNews(news);
        redirectAttributes.addFlashAttribute("message", "구단소식이 성공적으로 추가되었습니다.");
        return "redirect:/admin/layout/news_management";
    }

    // 구단소식 수정
    @PostMapping("/layout/newsUpdate/{newsIdx}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateNews(@PathVariable int newsIdx, @ModelAttribute News news, RedirectAttributes redirectAttributes) {

        news.setNewsIdx(newsIdx);
        adminService.updateNews(news);
        redirectAttributes.addFlashAttribute("message", "구단소식이 성공적으로 업데이트되었습니다.");
        return "redirect:/admin/layout/news_management";
    }

    // 구단소식 삭제
    @PostMapping("/layout/newsDelete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> newsDelete(@RequestParam("newsIdx") List<Integer> newsIdxs) {
        try {
            adminService.newsDelete(newsIdxs);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // ================================== News End


    // ================================== ClubPhoto start


    // 구단사진 정보 가져오기
    @GetMapping("/layout/getClubPhotoInfo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ClubPhoto>> getClubPhotoInfo() {
        List<ClubPhoto> clubPhotos = adminService.getAllClubPhoto();
        System.out.println("clubPhotos = " + clubPhotos);
        return ResponseEntity.ok(clubPhotos);
    }

    // 구단사진 상세 정보 가져오기
    @GetMapping("/layout/clubPhotoView")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ClubPhoto> getClubPhotoView(@RequestParam("cpIdx") int cpIdx, HttpServletResponse response) {
        ClubPhoto clubPhotos = adminService.findClubPhotoById(cpIdx);
        System.out.println("clubPhotoView = " + clubPhotos);
        if (clubPhotos != null) {
            return ResponseEntity.ok(clubPhotos);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 구단사진 작성
    @PostMapping("/layout/clubPhotoPost")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String postClubPhoto(@ModelAttribute ClubPhoto clubPhoto, @RequestParam String username, RedirectAttributes redirectAttributes) {
        clubPhoto.setUsername(username);
        adminService.saveClubPhoto(clubPhoto);
        redirectAttributes.addFlashAttribute("message", "구단사진이 성공적으로 추가되었습니다.");
        return "redirect:/admin/layout/photo_management";
    }

    // 구단사진 수정
    @PostMapping("/layout/clubPhotoUpdate/{cpIdx}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateClubPhoto(@PathVariable int cpIdx, @ModelAttribute ClubPhoto clubPhoto, RedirectAttributes redirectAttributes) {

        clubPhoto.setCpIdx(cpIdx);
        adminService.updateClubPhoto(clubPhoto);
        redirectAttributes.addFlashAttribute("message", "구단사진이 성공적으로 업데이트되었습니다.");
        return "redirect:/admin/layout/photo_management";
    }

    // 구단사진 삭제
    @PostMapping("/layout/clubPhotoDelete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> clubPhotoDelete(@RequestParam("cpIdx") List<Integer> cpIdxs) {
        try {
            adminService.clubPhotoDelete(cpIdxs);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // ================================== ClubPhoto End


    // ================================== ClubVideo start


    // 구단영상 정보 가져오기
    @GetMapping("/layout/getClubVideoInfo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<ClubVideo>> getClubVideoInfo() {
        List<ClubVideo> clubVideos = adminService.getAllClubVideo();
        System.out.println("clubVideos = " + clubVideos);
        return ResponseEntity.ok(clubVideos);
    }


    // 구단영상 상세 정보 가져오기
    @GetMapping("/layout/clubVideoView")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ClubVideo> getClubVideoView(@RequestParam("cvIdx") int cvIdx, HttpServletResponse response) {
        ClubVideo clubVideos = adminService.findClubVideoById(cvIdx);
        System.out.println("clubVideoView = " + clubVideos);
        if (clubVideos != null) {
            return ResponseEntity.ok(clubVideos);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


/*    // 구단영상 업로드
    @PostMapping("/layout/youtube")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView youtube(@ModelAttribute ClubVideo clubVideo, @RequestParam String username) throws Exception {
        clubVideo.setUsername(username);
        clubVideo.setMvTheOriginUrl("https://youtu.be/" + clubVideo.getMvTheOriginUrl());

        adminService.saveClubVideo(clubVideo);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/admin/layout/video_management");
        mav.addObject("clubVideo", clubVideo); // 뷰에 전달할 데이터 설정

        return mav;
    }*/


      /* // 구단영상 수정
    @PostMapping("/layout/clubVideoUpdate/{cvIdx}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateClubVideo(@PathVariable int cvIdx, @ModelAttribute ClubVideo clubVideo, RedirectAttributes redirectAttributes) {
        clubVideo.setCvIdx(cvIdx);
        clubVideo.setMvTheOriginUrl("https://youtu.be/" + clubVideo.getMvTheOriginUrl());
        System.out.println("clubVideo = " + clubVideo.getMvTheOriginUrl());
        adminService.updateClubVideo(clubVideo);
        redirectAttributes.addFlashAttribute("message", "구단영상이 성공적으로 업데이트되었습니다.");
        return "redirect:/admin/layout/video_management";
    }*/


    // 구단영상 업로드
    @PostMapping("/layout/youtube")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView youtube(@ModelAttribute ClubVideo clubVideo, @RequestParam String username) throws Exception {
        clubVideo.setUsername(username);
        // 사용자로부터 받은 영상 ID를 임베드 URL 형식으로 변환하여 저장
        String embedUrl = "https://www.youtube.com/embed/" + clubVideo.getMvTheOriginUrl();
        clubVideo.setMvTheOriginUrl(embedUrl);

        adminService.saveClubVideo(clubVideo);

        ModelAndView mav = new ModelAndView();
        mav.setViewName("redirect:/admin/layout/video_management");
        mav.addObject("clubVideo", clubVideo); // 뷰에 전달할 데이터 설정

        return mav;
    }

    // 구단영상 수정
    @PostMapping("/layout/clubVideoUpdate/{cvIdx}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateClubVideo(@PathVariable int cvIdx, @ModelAttribute ClubVideo clubVideo, RedirectAttributes redirectAttributes) {
        clubVideo.setCvIdx(cvIdx);
        // 사용자로부터 받은 영상 ID를 임베드 URL 형식으로 변환하여 저장
        String embedUrl = "https://www.youtube.com/embed/" + clubVideo.getMvTheOriginUrl();
        clubVideo.setMvTheOriginUrl(embedUrl);
        System.out.println("clubVideo = " + clubVideo.getMvTheOriginUrl());
        adminService.updateClubVideo(clubVideo);
        redirectAttributes.addFlashAttribute("message", "구단영상이 성공적으로 업데이트되었습니다.");
        return "redirect:/admin/layout/video_management";
    }


    // 구단영상 삭제
    @PostMapping("/layout/clubVideoDelete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> clubVideoDelete(@RequestParam("cvIdx") List<Integer> cvIdxs) {
        try {
            adminService.clubVideoDelete(cvIdxs);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // ================================== ClubVideo End


    // ================================== players start

    /*수정 전*/
   /* // K5 선수 정보 가져오기
    @GetMapping("/layout/get_k5PlayerInfo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<K5_Player>> get_k5PlayerInfo() {
        List<K5_Player> k5Players = adminService.getK5Player();
        System.out.println("k5Players = " + k5Players);
        return ResponseEntity.ok(k5Players);
    }*/

      /*// K7 선수 정보 가져오기
    @GetMapping("/layout/get_k7PlayerInfo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<K7_Player>> get_k7PlayerInfo() {
        List<K7_Player> k7Players = adminService.getK7Player();
        System.out.println("k7Players = " + k7Players);
        return ResponseEntity.ok(k7Players);
    }

    // W1리그 선수 정보 가져오기
    @GetMapping("/layout/get_w1PlayerInfo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<W1_Player>> get_w1PlayerInfo() {
        List<W1_Player> w1Players = adminService.getW1Player();
        System.out.println("w1Players = " + w1Players);
        return ResponseEntity.ok(w1Players);
    }*/

    // K5 선수 정보와 관련 파일 정보 가져오기
    @GetMapping("/layout/get_k5PlayerInfo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> get_k5PlayerInfo() {

        List<K5_Player> k5Players = adminService.getK5Player();
        List<Map<String, Object>> playerInfoWithFiles = new ArrayList<>();

        for (K5_Player player : k5Players) {
            Map<String, Object> playerInfo = new HashMap<>();
            List<File> files = adminService.findFilesByK5PlayerNum(player.getK5PlayerNum());

            playerInfo.put("player", player);
            playerInfo.put("files", files);
            playerInfoWithFiles.add(playerInfo);
        }

        return ResponseEntity.ok(playerInfoWithFiles);
    }


    // K7 선수 정보와 관련 파일 정보 가져오기
    @GetMapping("/layout/get_k7PlayerInfo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> get_k7PlayerInfo() {
        List<K7_Player> k7Players = adminService.getK7Player();
        List<Map<String, Object>> playerInfoWithFiles = new ArrayList<>();

        for (K7_Player player : k7Players) {
            Map<String, Object> playerInfo = new HashMap<>();
            List<File> files = adminService.findFilesByK7PlayerNum(player.getK7PlayerNum());

            playerInfo.put("player", player);
            playerInfo.put("files", files);
            playerInfoWithFiles.add(playerInfo);
        }

        return ResponseEntity.ok(playerInfoWithFiles);
    }

    // W1 선수 정보와 관련 파일 정보 가져오기
    @GetMapping("/layout/get_w1PlayerInfo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Map<String, Object>>> get_w1PlayerInfo() {
        List<W1_Player> w1Players = adminService.getW1Player();
        List<Map<String, Object>> playerInfoWithFiles = new ArrayList<>();

        for (W1_Player player : w1Players) {
            Map<String, Object> playerInfo = new HashMap<>();
            List<File> files = adminService.findFilesByW1PlayerNum(player.getW1PlayerNum());

            playerInfo.put("player", player);
            playerInfo.put("files", files);
            playerInfoWithFiles.add(playerInfo);
        }

        return ResponseEntity.ok(playerInfoWithFiles);
    }


    // k5 선수 상세 정보와 관련 파일(프로필 사진 등) 가져오기
    @GetMapping("/layout/k5PlayerView")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Object>> getK5PlayerView(@RequestParam("playerNum") int playerNum) {

        K5_Player k5Player = adminService.find_k5PlayerByNum(playerNum);
        Map<String, Object> response = new HashMap<>();
        if (k5Player != null) {
            // 선수 정보를 response 맵에 추가
            response.put("player", k5Player);

            // 해당 선수번호로 파일 정보 조회
            List<File> files = adminService.findFilesByK5PlayerNum(playerNum);

            // 파일 정보를 response 맵에 추가
            response.put("files", files);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // k7 선수 상세 정보와 관련 파일(프로필 사진 등) 가져오기
    @GetMapping("/layout/k7PlayerView")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Object>> getK7PlayerView(@RequestParam("playerNum") int playerNum) {
        K7_Player k7Player = adminService.find_k7PlayerByNum(playerNum);
        Map<String, Object> response = new HashMap<>();
        if (k7Player != null) {
            // 선수 정보를 response 맵에 추가
            response.put("player", k7Player);

            // 해당 선수번호로 파일 정보 조회
            List<File> files = adminService.findFilesByK7PlayerNum(playerNum);

            // 파일 정보를 response 맵에 추가
            response.put("files", files);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    // w1 선수 상세 정보와 관련 파일(프로필 사진 등) 가져오기
    @GetMapping("/layout/w1PlayerView")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Object>> getW1PlayerView(@RequestParam("playerNum") int playerNum) {
        W1_Player w1Player = adminService.find_w1PlayerByNum(playerNum);
        Map<String, Object> response = new HashMap<>();
        if (w1Player != null) {
            // 선수 정보를 response 맵에 추가
            response.put("player", w1Player);

            // 해당 선수번호로 파일 정보 조회
            List<File> files = adminService.findFilesByW1PlayerNum(playerNum);

            // 파일 정보를 response 맵에 추가
            response.put("files", files);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    // 선수 삭제
    @PostMapping("/layout/playerDelete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> playerDelete(@RequestParam("playerNums") List<Integer> playerNums, @RequestParam("playerType") String playerType, HttpServletResponse response) {
        System.out.println("컨트롤러 playerNums = " + playerNums + ", playerType = " + playerType);
        try {
            // 한 번의 호출로 모든 선수 번호 처리
            adminService.playerDelete(playerNums, playerType);
            System.out.println("선수 삭제 완료!!");

            fileService.deleteS3FilesByPlayerNumAndType(playerNums, playerType);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // 선수 등록
    @PostMapping("/layout/insertPlayerInfo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> insertPlayerInfo(
            @RequestParam("selectedPlayerType") String selectedPlayerType,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam Map<String, String> otherParams
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            // otherParams를 Map<String, Object>로 변환
            Map<String, Object> paramMap = new HashMap<>(otherParams);
            // playerNum 키를 동적으로 생성 및 접근
            String playerNumKey = selectedPlayerType + "PlayerNum";
            String playerNum = paramMap.get(playerNumKey).toString(); // 키 사용

            if (file != null && playerNum != null) {
                // 파일 및 선수 번호가 제공되었을 때 로직 처리
                String fileUrl = fileService.uploadPlayerPhotoToS3AndSaveMetadata(file, selectedPlayerType, playerNum);
                paramMap.put("fileUrl", fileUrl);
            } else {
                // 필수 데이터가 누락된 경우의 에러 처리
                response.put("errorMessage", "필수 데이터 누락: 선수 번호 또는 파일이 없습니다.");
                return ResponseEntity.badRequest().body(response);
            }

            // 선수 등록 서비스 호출
            adminService.registerPlayer(paramMap, selectedPlayerType);

            // 성공 메시지 응답
            response.put("message", "선수 등록이 성공적으로 완료되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // 예외 상세 내용을 로그로 출력
            response.put("errorMessage", "업로드 실패: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }


    // 선수 업데이트
    @PostMapping("/layout/updatePlayerInfo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updatePlayerInfo(
            @RequestParam("selectedPlayerType") String selectedPlayerType,
            @RequestParam("playerNum") int playerNum,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam Map<String, String> otherParams, // 나머지 모든 파라미터를 담을 맵
            HttpServletRequest request) {

        System.out.println("=========== selectedPlayerType = " + selectedPlayerType);
        System.out.println("=========== playerNum = " + playerNum);
        System.out.println("=========== otherParams = " + otherParams);

        try {
            // 기존 파일 정보 삭제 로직 추가
            fileService.deleteFilesByPlayerNumAndType(playerNum, selectedPlayerType);

            // 파일 서비스를 이용한 새 파일 업로드
            String fileUrl = null; // 파일 URL 초기화
            if (file != null && !file.isEmpty()) {
                // 파일이 존재하는 경우, 파일을 업로드하고 메타데이터 저장
                fileUrl = fileService.uploadPlayerPhotoToS3AndSaveMetadata(file, selectedPlayerType, String.valueOf(playerNum));
            }

            // paramMap에 필요한 데이터를 추가하고 수정 로직 호출
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("playerNum", playerNum); // playerNum 추가
            paramMap.put("fileUrl", fileUrl); // 파일 URL 추가

            // otherParams에서 받은 나머지 파라미터들을 paramMap에 추가
            for (Map.Entry<String, String> entry : otherParams.entrySet()) {
                paramMap.put(entry.getKey(), entry.getValue());
            }

             adminService.updatePlayer(paramMap, selectedPlayerType);

            // 성공 응답 반환
            return ResponseEntity.ok(Map.of("message", "선수 정보가 성공적으로 처리되었습니다."));
        } catch (Exception e) {
            // 예외 발생 시, 오류 응답 반환
            return ResponseEntity.badRequest().body(Map.of("errorMessage", "처리 실패: " + e.getMessage()));
        }
    }


    // K5 선수 번호 검사
    @GetMapping("/layout/check_k5PlayerNumber")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Boolean>> checkK5PlayerNumber(@RequestParam("playerNum") int playerNum) {
        Map<String, Boolean> response = new HashMap<>();
        boolean isUsed = adminService.existsK5PlayerByNum(playerNum);
        response.put("isUsed", isUsed);
        return ResponseEntity.ok(response);
    }

    // K7 선수 번호 검사
    @GetMapping("/layout/check_k7PlayerNumber")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Boolean>> checkK7PlayerNumber(@RequestParam("playerNum") int playerNum) {
        Map<String, Boolean> response = new HashMap<>();
        boolean isUsed = adminService.existsK7PlayerByNum(playerNum);
        response.put("isUsed", isUsed);
        return ResponseEntity.ok(response);
    }

    // W1 선수 번호 검사
    @GetMapping("/layout/check_w1PlayerNumber")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Boolean>> checkW1PlayerNumber(@RequestParam("playerNum") int playerNum) {
        Map<String, Boolean> response = new HashMap<>();
        boolean isUsed = adminService.existsW1PlayerByNum(playerNum);
        response.put("isUsed", isUsed);
        return ResponseEntity.ok(response);
    }




    // ================================== Players End


    // ================================== Staff start

    // 스태프 선수 정보 가져오기
    @GetMapping("/layout/get_staffInfo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<TeamStaff>> get_staffInfo() {
        List<TeamStaff> teamStaffs = adminService.getTeamStaff();
        return ResponseEntity.ok(teamStaffs);
    }


    // 구단 Staff 상세 정보 가져오기
    @GetMapping("/layout/teamStaffView")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TeamStaff> teamStaffView(@RequestParam("teamStaffNum") int teamStaffNum, HttpServletResponse response) {
        TeamStaff teamStaff = adminService.findTeamStaffByNum(teamStaffNum);
        System.out.println("컨트롤러 teamStaff = " + teamStaff);
        if (teamStaff != null) {
            return ResponseEntity.ok(teamStaff);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    // 구단 스태프 삭제
    @PostMapping("/layout/teamStaffDelete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> teamStaffDelete(@RequestParam("teamStaffNum") List<Integer> teamStaffNum, HttpServletResponse response) {
        System.out.println("컨트롤러 teamStaffNum = " + teamStaffNum);
        try {
            // 한 번의 호출로 모든 번호 처리
            adminService.teamStaffDelete(teamStaffNum);
            System.out.println("TeamStaff 삭제 완료!!");

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();

            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 스태프 등록
    @PostMapping("/layout/insertStaffInfo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> insertStaffInfo(
            @ModelAttribute TeamStaff teamStaff,
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam Map<String, String> otherParams
    ) {
        Map<String, Object> response = new HashMap<>();
        try {
            Map<String, Object> paramMap = new HashMap<>(otherParams);

            // 파일이 제공되었을 경우 파일 저장 로직 수행
            if (file != null && !file.isEmpty()) {
                String fileUrl = fileService.uploadStaffPhotoToS3AndSaveMetadata(file, teamStaff.getTeamLeagueGb(), String.valueOf(teamStaff.getTeamStaffNum()));
                paramMap.put("fileUrl", fileUrl);
            }
            System.out.println("컨 ======== teamStaff = " + teamStaff);
            // TeamStaff 객체 저장
            adminService.teamStaffSave(teamStaff);

            // 성공 메시지 응답
            response.put("message", "스태프 등록이 성공적으로 완료되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            // 예외 발생시 로그 출력 및 에러 메시지 응답
            response.put("error", "스태프 등록 처리 중 에러가 발생하였습니다.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }




    /* // 구단 스태프 등록
    @PostMapping("/layout/teamStaffUpdate")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String teamStaffUpdate(@ModelAttribute TeamStaff teamStaff,
                                  @RequestParam(value = "file", required = false) MultipartFile file,
                                  RedirectAttributes redirectAttributes) {


        if (file != null && !file.isEmpty()) {
            String fileUrl = fileService.uploadStaffPhotoToS3AndSaveMetadata(file, teamLeagueGb, teamStaffNum);
        }

        // TeamStaff 객체 저장
        adminService.save(teamStaff);

        // 리다이렉트 후 메시지 표시
        redirectAttributes.addFlashAttribute("message", "스태프 정보가 성공적으로 추가되었습니다.");

        return "redirect:/admin/layout/staff";
    }
*/




   /* // 구단 스태프 등록
    @PostMapping("/layout/teamStaffUpdate")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String teamStaffUpdate(@RequestBody TeamStaff teamStaff, RedirectAttributes redirectAttributes) {

        System.out.println("컨트롤러 teamStaff = " + teamStaff);

        adminService.save(teamStaff);


        redirectAttributes.addFlashAttribute("message", "스태프 정보가 성공적으로 추가되었습니다.");

        return "redirect:/admin/layout/staff";
    }
*/

    // ================================== Staff End

    // ================================== Rule start


    // 규정 정보 가져오기
    @GetMapping("/layout/getRuleInfo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Rule>> getRuleInfo() {
        List<Rule> rules = adminService.getAllRule();
        System.out.println("rules = " + rules);
        return ResponseEntity.ok(rules);
    }


    // 규정 작성 (업로드 기능 추가)
    @PostMapping("/layout/rulePost")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String postRule(@ModelAttribute Rule rule,
                           @RequestParam String username,
                           @RequestParam("files") List<MultipartFile> files,
                           RedirectAttributes redirectAttributes) {
        rule.setUsername(username);
        int ruleNum = adminService.saveRule(rule); // 규정을 저장하고 그 ID를 가져옵니다.

        if (!files.isEmpty()) {
            // FileService를 이용하여 S3에 파일을 업로드하고 메타데이터를 저장
            fileService.uploadFilesToS3AndSaveMetadata(files, ruleNum, "ruleFiles");
        }

        redirectAttributes.addFlashAttribute("message", "규정이 성공적으로 추가되었습니다.");
        return "redirect:/admin/layout/rule_management";
    }


    // 규정 상세 정보 가져오기(업로드 파일 추가)
    @GetMapping("/layout/ruleView/{ruleNum}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getRuleDetails(@PathVariable int ruleNum) throws NotFoundException {
        adminService.increaseRuleHits(ruleNum);
        Rule rule = adminService.findRuleById(ruleNum);
        if (rule == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<File> files = adminService.findFilesByRuleNum(ruleNum); // 규정 번호에 해당하는 파일 목록 조회

        // 규정 정보와 파일 목록을 포함하는 객체 생성
        Map<String, Object> response = new HashMap<>();
        response.put("rule", rule);
        response.put("files", files);

        return ResponseEntity.ok(response);
    }


    // 규정 수정
    @PostMapping("/layout/ruleUpdate/{ruleNum}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateRule(@PathVariable int ruleNum,
                             @ModelAttribute Rule rule,
                             @RequestParam("files") List<MultipartFile> files,
                             RedirectAttributes redirectAttributes) {
        rule.setRuleNum(ruleNum);
        adminService.updateRule(rule); // 기존 규정 정보 업데이트

        // 기존 파일 정보 삭제
        fileService.deleteFilesByRuleNum(ruleNum);

        // 새로운 파일 처리
        if (!files.isEmpty()) {
            fileService.uploadFilesToS3AndSaveMetadata(files, ruleNum, "ruleFiles");
        }

        redirectAttributes.addFlashAttribute("message", "규정이 성공적으로 업데이트되었습니다.");
        return "redirect:/admin/layout/rule_management";
    }


    // 규정 삭제
    @PostMapping("/layout/ruleDelete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> ruleDelete(@RequestParam("ruleNum") List<Integer> ruleNums) {
        System.out.println("칸트롤러 입장");
        try {
            // 먼저 각 규정 번호에 해당하는 파일을 S3에서 삭제합니다.
            ruleNums.forEach(fileService::deleteFilesByRuleNum);
            // 각 규정 번호에 해당하는 이미지 파일을 S3에서 삭제
            ruleNums.forEach(imageService::deleteImageByRuleNum);

            // 모든 파일이 S3에서 삭제된 후, 데이터베이스에서 규정을 삭제합니다.
            adminService.ruleDelete(ruleNums);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // ================================== Rule End

    // ================================== Operation start

    // 경영공시 목록 가져오기
    @GetMapping("/layout/getOperationInfo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Operation>> getOperationInfo() {
        List<Operation> operations = adminService.getAllOperation();
        System.out.println("operations = " + operations);
        return ResponseEntity.ok(operations);
    }

    // 경영공시 작성 (업로드 기능 추가)
    @PostMapping("/layout/operationPost")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String postOperation(@ModelAttribute Operation operation,
                                @RequestParam String username,
                                @RequestParam("files") List<MultipartFile> files,
                                RedirectAttributes redirectAttributes) {
        operation.setUsername(username);
        int operationNum = adminService.saveOperation(operation); // 저장하고 그 ID를 가져옵니다.

        if (!files.isEmpty()) {
            // FileService를 이용하여 S3에 파일을 업로드하고 메타데이터를 저장
            fileService.uploadOperationFilesToS3AndSaveMetadata(files, operationNum, "operationFiles");
        }

        redirectAttributes.addFlashAttribute("message", "규정이 성공적으로 추가되었습니다.");
        return "redirect:/admin/layout/operation_management";
    }

    // 경영공시 상세 정보 가져오기(업로드 파일 추가)
    @GetMapping("/layout/operationView/{operationNum}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getOperationDetails(@PathVariable int operationNum) throws NotFoundException {
        adminService.increaseOperationHits(operationNum);
        Operation operation = adminService.findOperationById(operationNum);
        if (operation == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        List<File> files = adminService.findFilesByOperationNum(operationNum);

        // 규정 정보와 파일 목록을 포함하는 객체 생성
        Map<String, Object> response = new HashMap<>();
        response.put("operation", operation);
        response.put("files", files);

        return ResponseEntity.ok(response);
    }

    // 경영공시 수정
    @PostMapping("/layout/operationUpdate/{operationNum}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateOperation(@PathVariable int operationNum,
                                  @ModelAttribute Operation operation,
                                  @RequestParam("files") List<MultipartFile> files,
                                  RedirectAttributes redirectAttributes) {
        operation.setOperationNum(operationNum);
        adminService.updateOperation(operation); // 기존 규정 정보 업데이트

        // 기존 파일 정보 삭제
        fileService.deleteFilesByOperationNum(operationNum);

        // 새로운 파일 처리
        if (!files.isEmpty()) {
            fileService.uploadOperationFilesToS3AndSaveMetadata(files, operationNum, "ruleFiles");
        }

        redirectAttributes.addFlashAttribute("message", "경영공시가 성공적으로 업데이트되었습니다.");
        return "redirect:/admin/layout/operation_management";
    }

    // 경영공시 삭제
    @PostMapping("/layout/operationDelete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> operationDelete(@RequestParam("operationNum") List<Integer> operationNums) {
        try {
            // 먼저 각 번호에 해당하는 파일을 S3에서 삭제합니다.
            operationNums.forEach(fileService::deleteFilesByOperationNum);
            // 각 번호에 해당하는 이미지 파일을 S3에서 삭제
            operationNums.forEach(imageService::deleteImageByOperationNum);

            // 모든 파일이 S3에서 삭제된 후, 데이터베이스에서 규정을 삭제합니다.
            adminService.operationDelete(operationNums);

            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    // ================================== Operation End


    // ================================== Faq start


    // FAQ 정보 가져오기
    @GetMapping("/layout/getFaqInfo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<Faq>> getFaqInfo() {
        List<Faq> faqs = adminService.getAllFaq();
        System.out.println("faqs = " + faqs);
        return ResponseEntity.ok(faqs);
    }


    // Faq 등록
    @PostMapping("/layout/faqPost")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String faqPost(@ModelAttribute Faq faq,
                          RedirectAttributes redirectAttributes) {
        adminService.saveFaq(faq);
        redirectAttributes.addFlashAttribute("message", "FAQ가 성공적으로 추가되었습니다.");
        return "redirect:/admin/layout/faq_management";
    }


    // FAQ 상세 정보 보기
    @GetMapping("/layout/faqView/{faqId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getFaqDetails(@PathVariable int faqId) throws NotFoundException {
        Faq faq = adminService.findFaqById(faqId);
        if (faq == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("faq", faq);

        return ResponseEntity.ok(response);
    }

    // FAQ 수정
    @PostMapping("/layout/faqUpdate/{faqId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateFaq(@PathVariable int faqId,
                            @ModelAttribute Faq faq,
                            RedirectAttributes redirectAttributes) {
        faq.setFaqId(faqId);
        adminService.updateFaq(faq); // 기존 규정 정보 업데이트


        redirectAttributes.addFlashAttribute("message", "Faq가 성공적으로 업데이트되었습니다.");
        return "redirect:/admin/layout/faq_management";
    }

    // FAQ 삭제
    @PostMapping("/layout/faqDelete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> faqDelete(@RequestParam("faqId") List<Integer> faqIds) {
        try {
            adminService.faqDelete(faqIds);
            return ResponseEntity.ok().body("선택한 글이 모두 삭제되었습니다.");
        } catch (Exception e) {
            // 실패 시, 에러 메시지와 함께 BadRequest를 반환합니다.
            return ResponseEntity.badRequest().body("글 삭제에 실패하였습니다. " + e.getMessage());
        }
    }


    // ================================== Faq End

    // ================================== 훈련일정 start

    // 훈련일정 목록 가져오기
    @GetMapping("/layout/getTrainingScheduleInfo")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<TrainingSchedule>> getTrainingScheduleInfo() {
        List<TrainingSchedule> trainingSchedules = adminService.getAllTrainingSchedule();
        System.out.println("trainingSchedules = " + trainingSchedules);
        return ResponseEntity.ok(trainingSchedules);
    }


    // 훈련일정 작성
    @PostMapping("/layout/trainingSchedulePost")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String postTrainingSchedule(@ModelAttribute TrainingSchedule trainingSchedule,
                                       @RequestParam String username,
                                       RedirectAttributes redirectAttributes) {
        trainingSchedule.setUsername(username);
        int scheduleNum = adminService.saveTrainingSchedule(trainingSchedule); // 저장하고 그 ID를 가져옵니다.


        redirectAttributes.addFlashAttribute("message", "일정이 성공적으로 추가되었습니다.");
        return "redirect:/admin/layout/trainingSchedule_management";
    }


    // 훈련일정 상세 정보 가져오기
    @GetMapping("/layout/trainingScheduleView/{scheduleNum}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> getTrainingScheduleDetails(@PathVariable int scheduleNum) throws NotFoundException {
        adminService.increaseTrainingSchedule(scheduleNum);
        TrainingSchedule trainingSchedule = adminService.findTrainingScheduleByNum(scheduleNum);
        if (trainingSchedule == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("trainingSchedule", trainingSchedule);

        return ResponseEntity.ok(response);
    }

    // 훈련일정 수정
    @PostMapping("/layout/trainingScheduleUpdate/{scheduleNum}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateTrainingSchedule(@PathVariable int scheduleNum,
                                         @ModelAttribute TrainingSchedule trainingSchedule,
                                         RedirectAttributes redirectAttributes) {
        trainingSchedule.setScheduleNum(scheduleNum);
        adminService.updateTrainingSchedule(trainingSchedule);

        redirectAttributes.addFlashAttribute("message", "훈련일정이 성공적으로 업데이트되었습니다.");
        return "redirect:/admin/layout/trainingSchedule_management";
    }

    // 훈련일정 삭제
    @PostMapping("/layout/trainingScheduleDelete")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> trainingScheduleDelete(@RequestParam("scheduleNum") List<Integer> scheduleNums) {
        try {
            adminService.trainingScheduleDelete(scheduleNums);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    // ================================== Faq End













    // ================================== Page 이동

    @GetMapping("/layout/adminpage")
    public String adminPage(Model model) {
        model.addAttribute("currentPage", "adminpage");
        return "layout/adminpage";
    }

    @GetMapping("/layout/qna")
    public String oneOneContact(Model model) {
        model.addAttribute("currentPage", "qna");
        return "layout/common/board/qna";
    }

    @GetMapping("/layout/players")
    public String players(Model model) {
        model.addAttribute("currentPage", "players");
        return "layout/common/board/players";
    }

    @GetMapping("/layout/staff")
    public String staff(Model model) {
        model.addAttribute("content", "스태프 관리 입장");
        model.addAttribute("currentPage", "staff");
        return "layout/common/board/staff";
    }

    @GetMapping("/layout/k4match")
    public String k4match(Model model) {
        model.addAttribute("content", "K4 매치 관리 입장");
        model.addAttribute("currentPage", "k4match");
        return "layout/common/board/k4match";
    }

    @GetMapping("/layout/k5match")
    public String k5match(Model model) {
        model.addAttribute("content", "K5 매치 관리 입장");
        model.addAttribute("currentPage", "k5match");
        return "layout/common/board/k5match";
    }

    @GetMapping("/layout/k7match")
    public String k7match(Model model) {
        model.addAttribute("content", "K7 매치 관리 입장");
        model.addAttribute("currentPage", "k7match");
        return "layout/common/board/k7match";
    }

    @GetMapping("/layout/wLeaguematch")
    public String wLeaguematch(Model model) {
        model.addAttribute("content", "W리그 매치 관리 입장");
        model.addAttribute("currentPage", "sLeaguematch");
        return "layout/common/board/wLeaguematch";
    }

    @GetMapping("/layout/notice_management")
    public String noticeManagement(Model model) {
        model.addAttribute("currentPage", "notice_management");
        return "layout/common/board/notice_management";
    }

    @GetMapping("/layout/news_management")
    public String newsManagement(Model model) {
        model.addAttribute("currentPage", "news_management");
        return "layout/common/board/news_management";
    }

    @GetMapping("/layout/photo_management")
    public String photoManagement(Model model) {
        model.addAttribute("content", "구단사진 관리 입장");
        model.addAttribute("currentPage", "photo_management");
        return "layout/common/board/photo_management";
    }

    @GetMapping("/layout/video_management")
    public String videoManagement(Model model) {
        model.addAttribute("content", "구단영상 관리 입장");
        model.addAttribute("currentPage", "video_management");
        return "layout/common/board/video_management";
    }

    @GetMapping("/layout/video_management2")
    public String videoManagement2(Model model) {
        model.addAttribute("content", "구단영상2 관리 입장");
        model.addAttribute("currentPage", "video_management222");
        return "layout/common/board/video_management222";
    }


    @GetMapping("/layout/rule_management")
    public String ruleManagement(Model model) {
        model.addAttribute("content", "규정 관리 입장");
        model.addAttribute("currentPage", "rule_management");
        return "layout/common/board/rule_management";
    }

    @GetMapping("/layout/operation_management")
    public String operationManagement(Model model) {
        model.addAttribute("content", "경영 공시 관리 입장");
        model.addAttribute("currentPage", "operation_management");
        return "layout/common/board/operation_management";
    }

    @GetMapping("/layout/faq_management")
    public String faqManagement(Model model) {
        model.addAttribute("content", "FAQ 관리 입장");
        model.addAttribute("currentPage", "faq_management");
        return "layout/common/board/faq_management";
    }

    @GetMapping("/layout/trainingSchedule_management")
    public String trainingScheduleManagement(Model model) {
        model.addAttribute("content", "훈련일정 관리 입장");
        model.addAttribute("currentPage", "trainingSchedule_management");
        return "layout/common/board/trainingschedule_management";
    }

// ================================== page 이동 and

}
