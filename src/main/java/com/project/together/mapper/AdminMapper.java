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

    List<File> findFilesByNoticeNum(int noticeNum);

    void insertNotice(Notice notice);

    void insertNoticeFile(File file);

    void updateNotice(Notice notice);

    void deleteNotice(List<Integer> noticeNums);

    Notice findNoticesByNoticeNum(int noticeNum);

    void updateNoticeHits(@Param("noticeNum") int noticeNum, @Param("noticeHits") int noticeHits);

    void deleteFilesByNoticeNum(int noticeNum);


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

    void insertPlayerFile(File photoMetadata);


    /* ======================================= */

    List<TeamStaff> getTeamStaff();

    void insertTeamStaff(TeamStaff teamStaff);

    void deleteTeamStaff(List<Integer> teamStaffNum);

    TeamStaff findTeamStaffByNum(int teamStaffNum);

    /* ======================================= */

    List<Rule> getAllRule();

    Rule findRuleById(@Param("ruleNum") int ruleNum);

    /*void insertRule(Rule rule);*/

    void updateRule(Rule rule);

    void deleteRule(List<Integer> ruleNums);

    void insertRuleFile(File file);

    void insertRule(Rule rule);

    List<File> findFilesByRuleNum(int ruleNum);

    void deleteFilesByRuleNum(int ruleNum);

    List<String> findImageUrlsByRuleNum(int ruleNum);

    Rule findRuleByRuleNum(int ruleNum);

    void updateRuleHits(@Param("ruleNum") int ruleNum, @Param("ruleHits") int ruleHits);


    /* ======================================= */

    List<Operation> getAllOperation();

    Operation findOperationById(@Param("operationNum") int operationNum);

    void updateOperation(Operation operation);

    void deleteOperation(List<Integer> operationNums);

    void insertOperation(Operation operation);

    List<File> findFilesByOperationNum(int operationNum);

    void deleteFilesByOperationNum(int operationNum);

    void insertOperationFile(File fileMetadata);


    List<String> findImageUrlsByOperationNum(int operationNum);

    Operation findOperationByOperationNum(int operationNum);

    void updateOperationHits(@Param("operationNum") int operationNum, @Param("operationHits") int operationHits);

    /* ======================================= */

    List<Faq> getAllFaq();

    Faq findFaqById(@Param("faqId") int faqId);

    void insertFaq(Faq faq);

    void updateFaq(Faq faq);

    void deleteFaq(List<Integer> faqIds);

    /* ======================================= */

    List<TrainingSchedule> getAllTrainingSchedule();

    void insertTrainingSchedule(TrainingSchedule trainingSchedule);

    TrainingSchedule findScheduleByScheduleNum(int scheduleNum);

    void updateTrainingScheduleHits(@Param("scheduleNum") int scheduleNum, @Param("scheduleHits") int scheduleHits);

    void updateTrainingSchedule(TrainingSchedule trainingSchedule);

    void deleteTrainingSchedule(List<Integer> scheduleNums);

}
