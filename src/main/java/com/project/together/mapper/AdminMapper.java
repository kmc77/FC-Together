package com.project.together.mapper;

import com.project.together.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminMapper {

    List<User> getAllUsers();

    List<Qna> getAllQnAs();

    Qna findById(@Param("qnaNum") int qnaNum);

    int updateQnA(@Param("qnaNum") String qnaNum, @Param("authId") String authId, @Param("qnaReply") String qnaReply);

    void deleteQna(List<Integer> qnaNums);

    /* ======================================= */

    List<Notice> getAllNotice();

    Notice findNoticesById(@Param("noticeNum") int noticeNum);

    void insertNotice(Notice notice);

    void updateNotice(Notice notice);

    void deleteNotice(List<Integer> noticeNums);

    /* ======================================= */

    List<News> getAllNews();

    News findNewsById(@Param("newsIdx") int newsIdx);

    void insertNews(News news);

    void updateNews(News news);

    void deleteNews(List<Integer> newsIdxs);

    /* ======================================= */

    List<ClubPhoto> getAllClubPhoto();

    ClubPhoto findClubPhotoById(@Param("cpIdx") int cpIdx);

    void insertClubPhoto(ClubPhoto clubPhoto);

    void updateClubPhoto(ClubPhoto clubPhoto);

    void deleteClubPhoto(List<Integer> cpIdxs);

    /* ======================================= */

    List<ClubVideo> getAllClubVideo();

    ClubVideo findClubVideoById(@Param("cvIdx") int cvIdx);

    void insertClubVideo(ClubVideo clubVideo);

    void updateClubVideo(ClubVideo clubVideo);

    void deleteClubVideo(List<Integer> cvIdxs);


    /* ======================================= */

    List<K5_Player> getK5Player();

    List<K7_Player> getK7Player();

    List<W1_Player> getW1Player();

    K5_Player find_k5PlayerByNum(int playerNum);

    K7_Player find_k7PlayerByNum(int playerNum);

    W1_Player find_w1PlayerByNum(int playerNum);


    void registerK5Player(K5_Player k5Player);

    void registerK7Player(K7_Player k7Player);

    void registerW1Player(W1_Player w1Player);

    void deleteK5playerByPlayerNum(List<Integer> playerNums);

    void deleteK7playerByPlayerNum(List<Integer> playerNums);

    void deleteW1playerByPlayerNum(List<Integer> playerNums);

    /* ======================================= */

    List<TeamStaff> getTeamStaff();

    void insertTeamStaff(TeamStaff teamStaff);

    void deleteTeamStaff(List<Integer> teamStaffNum);

    TeamStaff findTeamStaffByNum(int teamStaffNum);
}
