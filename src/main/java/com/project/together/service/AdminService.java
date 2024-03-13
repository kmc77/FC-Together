package com.project.together.service;

import com.project.together.config.auth.PrincipalDetails;
import com.project.together.domain.*;
import com.project.together.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminMapper adminMapper;

    public List<PrincipalDetails> getAllUsers() {
        // 모든 사용자 정보를 가져옵니다.
        List<User> users = adminMapper.getAllUsers();

        // User 객체를 PrincipalDetails 객체로 변환한 후 리스트에 담습니다.
        List<PrincipalDetails> principalDetailsList = users.stream()
                .map(user -> new PrincipalDetails(user))
                .collect(Collectors.toList());

        return principalDetailsList;
    }

    // ================================== QnA start

    public List<Qna> getAllQnAs() {
        // 모든 QnA 정보를 가져옵니다.
        return adminMapper.getAllQnAs();
    }


    public Qna findById(int qnaNum) {
        return adminMapper.findById(qnaNum);
    }

    public boolean updateQnA(String qnaNum, String authId, String qnaReply) {
        int updatedRows = adminMapper.updateQnA(qnaNum, authId, qnaReply);
        return updatedRows > 0;
    }


    public void deleteQna(List<Integer> qnaNums) {
        // 데이터베이스에서 해당 qnaNum의 QnA들을 찾는다.
        for (int qnaNum : qnaNums) {
            Qna qna = adminMapper.findById(qnaNum);
            if (qna == null) {
                throw new IllegalArgumentException("해당 QnA가 존재하지 않습니다. qnaNum: " + qnaNum);
            }
        }

        // 찾은 QnA들을 삭제한다.
        adminMapper.deleteQna(qnaNums);
    }


    // ================================== QnA and

    // ================================== Notice start

    public List<Notice> getAllNotice() {
        // 모든 Notice 정보를 가져옵니다.
        return adminMapper.getAllNotice();
    }

    public Notice findNoticesById(int noticeNum) {
        return adminMapper.findNoticesById(noticeNum);
    }

    public void saveNotice(Notice notice) {
        // 현재 시간을 가져옴
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String nowStr = now.format(formatter);

        notice.setNoticeDate(nowStr);

        adminMapper.insertNotice(notice);
    }

    public void updateNotice(Notice notice) {
        // 현재 시간을 가져옴
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        // 시각을 'YYYY-MM-DD HH:MM:SS' 형식의 문자열로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String nowStr = now.format(formatter);

        notice.setNoticeDate(nowStr);

        adminMapper.updateNotice(notice);
    }

    public void noticeDelete(List<Integer> noticeNums) {
        //데이터베이스에서 해당 noticeNums 의 Notice 들을 찾는다.
        for (int noticeNum : noticeNums) {
            Notice notice = adminMapper.findNoticesById(noticeNum);
            if (notice == null) {
                throw new IllegalArgumentException("해당 Notice가 존재하지 않습니다. noticeNum: " + noticeNum);
            }
        }

        //찾은 Notice 글을 삭제
        adminMapper.deleteNotice(noticeNums);
    }

    // ================================== Notice and


    // ================================== News start


    public List<News> getAllNews() {
        // 모든 News 정보를 가져옵니다.
        return adminMapper.getAllNews();
    }

    public News findNewsById(int newsIdx) {
        return adminMapper.findNewsById(newsIdx);
    }

    public void saveNews(News news) {
        // 현재 시간을 가져옴
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String nowStr = now.format(formatter);

        news.setNewsRegdate(nowStr);

        adminMapper.insertNews(news);

    }

    public void updateNews(News news) {
        // 현재 시간을 가져옴
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        // 시각을 'YYYY-MM-DD HH:MM:SS' 형식의 문자열로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String nowStr = now.format(formatter);

        news.setNewsRegdate(nowStr);

        adminMapper.updateNews(news);

    }

    public void newsDelete(List<Integer> newsIdxs) {
        //데이터베이스에서 해당 noticeNums 의 Notice 들을 찾는다.
        for (int newsIdx : newsIdxs) {
            News news = adminMapper.findNewsById(newsIdx);
            if (news == null) {
                throw new IllegalArgumentException("해당 News가 존재하지 않습니다. newsIdx: " + newsIdx);
            }
        }

        //찾은 News 글을 삭제
        adminMapper.deleteNews(newsIdxs);
    }

    // ================================== News and


    // ================================== ClubPhoto start

    public List<ClubPhoto> getAllClubPhoto() {
        // 모든 ClubPhoto 정보를 가져옵니다.
        return adminMapper.getAllClubPhoto();
    }

    public ClubPhoto findClubPhotoById(int cpIdx) {
        return adminMapper.findClubPhotoById(cpIdx);
    }

    public void saveClubPhoto(ClubPhoto clubPhoto) {
        // 현재 시간을 가져옴
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String nowStr = now.format(formatter);

        clubPhoto.setCpRegdate(nowStr);

        adminMapper.insertClubPhoto(clubPhoto);
    }

    public void updateClubPhoto(ClubPhoto clubPhoto) {
        // 현재 시간을 가져옴
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        // 시각을 'YYYY-MM-DD HH:MM:SS' 형식의 문자열로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String nowStr = now.format(formatter);

        clubPhoto.setCpRegdate(nowStr);

        adminMapper.updateClubPhoto(clubPhoto);
    }

    public void clubPhotoDelete(List<Integer> cpIdxs) {
        //데이터베이스에서 해당 cpIdxs 의 clubPhoto 들을 찾는다.
        for (int cpIdx : cpIdxs) {
            ClubPhoto clubPhoto = adminMapper.findClubPhotoById(cpIdx);
            if (clubPhoto == null) {
                throw new IllegalArgumentException("해당 ClubPhoto가 존재하지 않습니다. cpIdx: " + cpIdx);
            }
        }

        //찾은 clubPhoto 글을 삭제
        adminMapper.deleteClubPhoto(cpIdxs);
    }


    // ================================== ClubPhoto and


    // ================================== ClubVideo start


    public List<ClubVideo> getAllClubVideo() {
        return adminMapper.getAllClubVideo();
    }

    public ClubVideo findClubVideoById(int cvIdx) {
        return adminMapper.findClubVideoById(cvIdx);
    }

    public void saveClubVideo(ClubVideo clubVideo) {
        // 현재 시간을 가져옴
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String nowStr = now.format(formatter);

        clubVideo.setCvDate(nowStr);

        adminMapper.insertClubVideo(clubVideo);

    }

    public void updateClubVideo(ClubVideo clubVideo) {
        // 현재 시간을 가져옴
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        // 시각을 'YYYY-MM-DD HH:MM:SS' 형식의 문자열로 변환
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String nowStr = now.format(formatter);

        clubVideo.setCvDate(nowStr);

        adminMapper.updateClubVideo(clubVideo);
    }

    public void clubVideoDelete(List<Integer> cvIdxs) {
        //데이터베이스에서 해당 cvIdxs 의 clubVideo 들을 찾는다.
        for (int cvIdx : cvIdxs) {
            ClubVideo clubVideo = adminMapper.findClubVideoById(cvIdx);
            if (clubVideo == null) {
                throw new IllegalArgumentException("해당 ClubVideo가 존재하지 않습니다. cvIdx: " + cvIdx);
            }
        }

        //찾은 clubVideo 글을 삭제
        adminMapper.deleteClubVideo(cvIdxs);
    }


    // ================================== ClubVideo and


    // ================================== Player start


    public List<K5_Player> getK5Player() {
        return adminMapper.getK5Player();
    }

    public List<K7_Player> getK7Player() {
        return adminMapper.getK7Player();
    }

    public List<S_Player> getSPlayer() {
        return adminMapper.getSPlayer();

    }

    public K5_Player find_k5PlayerByNum(int k5PlayerNum) {
        return adminMapper.find_k5PlayerByNum(k5PlayerNum);
    }


    // ================================== Player and
}