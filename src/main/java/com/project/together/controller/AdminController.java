package com.project.together.controller;

import com.project.together.config.auth.PrincipalDetails;
import com.project.together.domain.*;
import com.project.together.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
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

    // 공지사항 상세 정보 가져오기
    @GetMapping("/layout/noticeView")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Notice> getNoticeView(@RequestParam("noticeNum") int noticeNum, HttpServletResponse response) {
        Notice notice = adminService.findNoticesById(noticeNum);
        System.out.println("noticeview = " + notice);
        if (notice != null) {
            return ResponseEntity.ok(notice);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 공지사항 작성
    @PostMapping("/layout/noticePost")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String postNotice(@ModelAttribute Notice notice, @RequestParam String username, RedirectAttributes redirectAttributes) {
        notice.setUsername(username);
        adminService.saveNotice(notice);
        redirectAttributes.addFlashAttribute("message", "공지사항이 성공적으로 추가되었습니다.");
        return "redirect:/admin/layout/notice_management";
    }

    // 공지사항 수정
    @PostMapping("/layout/noticeUpdate/{noticeNum}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateNotice(@PathVariable int noticeNum, @ModelAttribute Notice notice, RedirectAttributes redirectAttributes) {

        notice.setNoticeNum(noticeNum);
        adminService.updateNotice(notice);
        redirectAttributes.addFlashAttribute("message", "공지사항이 성공적으로 업데이트되었습니다.");
        return "redirect:/admin/layout/notice_management";
    }

    // 공지사항 삭제
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
    }

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


    // 구단영상 업로드
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
    }


    // 구단영상 수정
    @PostMapping("/layout/clubVideoUpdate/{cvIdx}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String updateClubVideo(@PathVariable int cvIdx, @ModelAttribute ClubVideo clubVideo, RedirectAttributes redirectAttributes) {
        clubVideo.setCvIdx(cvIdx);
        clubVideo.setMvTheOriginUrl("https://youtu.be/" + clubVideo.getMvTheOriginUrl());
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








    // ================================== page 이동

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
        model.addAttribute("content", "선수단 관리 입장");
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

    @GetMapping("/layout/sLeaguematch")
    public String sLeaguematch(Model model) {
        model.addAttribute("content", "S리그 매치 관리 입장");
        model.addAttribute("currentPage", "sLeaguematch");
        return "layout/common/board/sLeaguematch";
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
