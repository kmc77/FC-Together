package com.project.together.mapper;

import com.project.together.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper
public interface AdminMapper {

    List<User> getAllUsers();

    void deleteUsers(@Param("userIds") List<Long> userIds);

    void deleteSection1Photos(@Param("photoIds") List<Long> photoIds);

    List<Qna> getAllQnAs();

    Qna findById(@Param("qnaNum") int qnaNum);

    int updateQnA(@Param("qnaNum") String qnaNum, @Param("authId") String authId, @Param("qnaReply") String qnaReply);

    void deleteQna(List<Integer> qnaNums);


    List<User> findUsersByIds(@Param("userIds") Set<Long> userIds);
    void deleteQnaByUsernames(@Param("usernames") List<String> usernames);
    void deleteUser(@Param("userIds") Set<Long> userIds);



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

    List<ClubPhoto> getSectionClubPhoto();

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

    List<File> findFilesByK5PlayerNum(int k5PlayerNum);

    List<File> findFilesByK7PlayerNum(int k7PlayerNum);

    List<File> findFilesByW1PlayerNum(int w1PlayerNum);

    boolean existsK5PlayerByNum(int playerNum);

    boolean existsK7PlayerByNum(int playerNum);

    boolean existsW1PlayerByNum(int playerNum);

    void deleteFilesByK5PlayerNum(int playerNum);

    void deleteFilesByK7PlayerNum(int playerNum);

    void deleteFilesByW1PlayerNum(int playerNum);

    void updateK5Player(K5_Player k5Player);

    void updateK7Player(K7_Player k7Player);

    void updateW1Player(W1_Player w1Player);


    /* ======================================= */

    List<TeamStaff> getTeamStaff();

    void insertTeamStaff(TeamStaff teamStaff);

    void deleteTeamStaff(List<Integer> teamStaffNum);

    TeamStaff findTeamStaffByNum(int teamStaffNum);

    void insertTeamStaffFile(File file);

    List<File> findFilesByTeamStaffNum(int teamStaffNum);

    List<File> findFilesByTeamStaffNums(List<Integer> teamStaffNum);

    void updateTeamStaff(TeamStaff teamStaff);

    void deleteFilesByTeamStaffNum(int teamStaffNum);

    void deleteFilesByTeamStaffNums(List<Integer> teamStaffNum);

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


    /* ======================================= */


    List<Team> getAllTeamList();

    List<Team> findTeamsByLeague(String league);

    /*void insertTeamFile(File logoMetadata);*/

    void registerTeam(Map<String, Object> paramMap);

    /*Team findTeamByName(int teamName);*/

    Team findTeamById(int id);

    void deleteTeamByTeamId(List<Integer> teamIds);


    /* ======================================= */

    List<Team> findK5TeamList(@Param("teamName") String teamName);


    List<Match> findK5MatchListByLeague(@Param("league") String league);


    void saveK5Match(Match matchRequest);

    Match findMatchById(int id);

    void updateMatch(Match existingMatch);

    void deleteMatch(int matchId);

    List<Team> findK7TeamList(@Param("teamName") String teamName);

    List<Match> findK7MatchListByLeague(@Param("league") String league);

    void saveK7Match(Match matchRequest);

    List<Team> findW1TeamList(@Param("teamName") String teamName);

    List<Match> findW1MatchListByLeague(@Param("league") String league);

    void saveW1Match(Match matchRequest);


}
