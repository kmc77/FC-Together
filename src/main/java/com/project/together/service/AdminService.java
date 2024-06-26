package com.project.together.service;

import com.project.together.config.auth.PrincipalDetails;
import com.project.together.domain.*;
import com.project.together.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminMapper adminMapper;



    @Value("${file.upload-dir}")
    private String uploadDir;

    // 날짜 포맷팅을 위한 메소드
    private String getCurrentFormattedTime() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return now.format(formatter);
    }



    public List<PrincipalDetails> getAllUsers() {
        // 모든 사용자 정보를 가져옵니다.
        List<User> users = adminMapper.getAllUsers();

        // User 객체를 PrincipalDetails 객체로 변환한 후 리스트에 담습니다.
        List<PrincipalDetails> principalDetailsList = users.stream()
                .map(user -> new PrincipalDetails(user))
                .collect(Collectors.toList());

        return principalDetailsList;
    }


    public void deleteUsers(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            throw new IllegalArgumentException("User ID list must not be null or empty");
        }

        // 중복된 ID 제거
        Set<Long> uniqueUserIds = userIds.stream().collect(Collectors.toSet());

        // uniqueUserIds 값을 이용해서 사용자 정보 추출
        List<User> users = adminMapper.findUsersByIds(uniqueUserIds);
        List<String> usernames = users.stream().map(User::getUsername).collect(Collectors.toList());

        // Qna 테이블에서 관련 레코드를 먼저 삭제합니다.
        adminMapper.deleteQnaByUsernames(usernames);

        // User 테이블에서 해당 사용자를 삭제합니다.
        adminMapper.deleteUser(uniqueUserIds);
    }

    public void deleteSection1Photos(List<Long> photoIds) {
        adminMapper.deleteSection1Photos(photoIds);
    }


    public List<Ranking> getAllRankings() {
        return adminMapper.getAllRankings();
    }


    public void updateRankings(List<Ranking> updatedRankings) {
        for (Ranking ranking : updatedRankings) {
            adminMapper.updateRanking(ranking);
        }
    }


    public void insertRanking(Ranking newRanking) {
        adminMapper.insertRanking(newRanking);
    }


    public void deleteRanking(String leagueGb, String teamName) {
        adminMapper.deleteRanking(leagueGb, teamName);
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

    public List<File> findFilesByNoticeNum(int noticeNum) {
        return adminMapper.findFilesByNoticeNum(noticeNum);
    }


    public int saveNotice(Notice notice) {
        notice.setNoticeDate(getCurrentFormattedTime());
        adminMapper.insertNotice(notice);
        return notice.getNoticeNum(); // MyBatis의 selectKey를 사용하여 생성된 ruleNum 반환
    }



    public void updateNotice(Notice notice) {
        notice.setNoticeDate(getCurrentFormattedTime());
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

    public void increaseNoticeHits(int noticeNum) throws NotFoundException {
        Notice notice = adminMapper.findNoticesByNoticeNum(noticeNum);
        if (notice == null) {
            throw new NotFoundException(noticeNum + "번 공지를 찾지 못 했습니다.");
        }
        notice.setNoticeHits(notice.getNoticeHits() + 1);
        adminMapper.updateNoticeHits(notice.getNoticeNum(), notice.getNoticeHits());
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
        news.setNewsRegdate(getCurrentFormattedTime()); // getCurrentFormattedTime 메서드 호출
        adminMapper.insertNews(news);

    }

    public void updateNews(News news) {
        news.setNewsRegdate(getCurrentFormattedTime());
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

    public List<ClubPhoto> getSectionClubPhoto() {
        return adminMapper.getSectionClubPhoto();
    }

    public ClubPhoto findClubPhotoById(int cpIdx) {
        return adminMapper.findClubPhotoById(cpIdx);
    }

    public void saveClubPhoto(ClubPhoto clubPhoto) {
        clubPhoto.setCpRegdate(getCurrentFormattedTime());
        adminMapper.insertClubPhoto(clubPhoto);
    }

    public void updateClubPhoto(ClubPhoto clubPhoto) {
        clubPhoto.setCpRegdate(getCurrentFormattedTime());
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

    public List<File> getSectionClubPhotoFiles() {
        return adminMapper.findFilesByTableGb("sectionClubPhoto");
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
        clubVideo.setCvDate(getCurrentFormattedTime());
        adminMapper.insertClubVideo(clubVideo);

    }

    public void updateClubVideo(ClubVideo clubVideo) {
        clubVideo.setCvDate(getCurrentFormattedTime());
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

    public List<W1_Player> getW1Player() {
        return adminMapper.getW1Player();

    }

    public K5_Player find_k5PlayerByNum(int playerNum) {
        return adminMapper.find_k5PlayerByNum(playerNum);
    }

    public K7_Player find_k7PlayerByNum(int playerNum) {
        return adminMapper.find_k7PlayerByNum(playerNum);
    }

    public W1_Player find_w1PlayerByNum(int playerNum) {
        return adminMapper.find_w1PlayerByNum(playerNum);
    }


    private K5_Player createK5Player(Map<String, Object> paramMap) {
        K5_Player k5_player = new K5_Player();

        try {
            k5_player.setK5PlayerNum(Integer.parseInt(paramMap.getOrDefault("k5PlayerNum", "0").toString()));
            k5_player.setK5PlayerName(paramMap.getOrDefault("k5PlayerName", "").toString());
            k5_player.setK5PlayerEnName(paramMap.getOrDefault("k5PlayerEnName", "").toString());
            k5_player.setK5PlayerCapYn(paramMap.getOrDefault("k5PlayerCapYn", "").toString());
            k5_player.setK5PlayerSubCapYn(paramMap.getOrDefault("k5PlayerSubCapYn", "").toString());
            k5_player.setK5PlayerPosition(paramMap.getOrDefault("k5PlayerPosition", "").toString());
            k5_player.setK5PlayerHeight(paramMap.getOrDefault("k5PlayerHeight", "").toString());
            k5_player.setK5PlayerWeight(paramMap.getOrDefault("k5PlayerWeight", "").toString());
            k5_player.setK5PlayerBirth(paramMap.getOrDefault("k5PlayerBirth", "").toString());
        } catch (Exception e) {
            throw new IllegalArgumentException("선수 정보 파싱 중 오류가 발생했습니다.", e);
        }

        return k5_player;
    }


    private K7_Player createK7Player(Map<String, Object> paramMap) {
        K7_Player k7_player = new K7_Player();
        // paramMap을 사용하여 k7_player의 필드를 채웁니다.

        try {
            k7_player.setK7PlayerNum(Integer.parseInt(paramMap.getOrDefault("k7PlayerNum", "0").toString()));
            k7_player.setK7PlayerName(paramMap.getOrDefault("k7PlayerName", "").toString());
            k7_player.setK7PlayerEnName(paramMap.getOrDefault("k7PlayerEnName", "").toString());
            k7_player.setK7PlayerCapYn(paramMap.getOrDefault("k7PlayerCapYn", "").toString());
            k7_player.setK7PlayerSubCapYn(paramMap.getOrDefault("k7PlayerSubCapYn", "").toString());
            k7_player.setK7PlayerPosition(paramMap.getOrDefault("k7PlayerPosition", "").toString());
            k7_player.setK7PlayerHeight(paramMap.getOrDefault("k7PlayerHeight", "").toString());
            k7_player.setK7PlayerWeight(paramMap.getOrDefault("k7PlayerWeight", "").toString());
            k7_player.setK7PlayerBirth(paramMap.getOrDefault("k7PlayerBirth", "").toString());
        } catch (Exception e) {
            throw new IllegalArgumentException("선수 정보 파싱 중 오류가 발생했습니다.", e);
        }
        return k7_player;
    }

    private W1_Player createW1Player(Map<String, Object> paramMap) {
        W1_Player w1_player = new W1_Player();
        // paramMap을 사용하여 w1_player의 필드를 채웁니다.

        try {
            w1_player.setW1PlayerNum(Integer.parseInt(paramMap.getOrDefault("w1PlayerNum", "0").toString()));
            w1_player.setW1PlayerName(paramMap.getOrDefault("w1PlayerName", "").toString());
            w1_player.setW1PlayerEnName(paramMap.getOrDefault("w1PlayerEnName", "").toString());
            w1_player.setW1PlayerCapYn(paramMap.getOrDefault("w1PlayerCapYn", "").toString());
            w1_player.setW1PlayerSubCapYn(paramMap.getOrDefault("w1PlayerSubCapYn", "").toString());
            w1_player.setW1PlayerPosition(paramMap.getOrDefault("w1PlayerPosition", "").toString());
            w1_player.setW1PlayerHeight(paramMap.getOrDefault("w1PlayerHeight", "").toString());
            w1_player.setW1PlayerWeight(paramMap.getOrDefault("w1PlayerWeight", "").toString());
            w1_player.setW1PlayerBirth(paramMap.getOrDefault("w1PlayerBirth", "").toString());
        } catch (Exception e) {
            throw new IllegalArgumentException("선수 정보 파싱 중 오류가 발생했습니다.", e);
        }
        return w1_player;
    }

    // 리그에 맞게 선수 등록
    public void registerPlayer(Map<String, Object> paramMap, String playerType) {
        System.out.println("서비스 -------- paramMap = " + paramMap);
        switch (playerType.toLowerCase()) {
            case "k5":
                K5_Player k5_player = createK5Player(paramMap);
                adminMapper.registerK5Player(k5_player);
                break;
            case "k7":
                K7_Player k7_player = createK7Player(paramMap);
                adminMapper.registerK7Player(k7_player);
                break;
            case "w1":
                W1_Player w1_player = createW1Player(paramMap);
                adminMapper.registerW1Player(w1_player);
                break;
            default:
                throw new IllegalArgumentException("유효하지 않은 선수 유형입니다.");
        }
    }

    public void playerDelete(List<Integer> playerNums, String playerType) {
        boolean playerExists = false;

        for (Integer playerNum : playerNums) {
            switch (playerType) {
                case "k5":
                    K5_Player k5Player = adminMapper.find_k5PlayerByNum(playerNum);
                    if (k5Player != null) {
                        adminMapper.deleteK5playerByPlayerNum(Collections.singletonList(k5Player.getK5PlayerNum()));
                        playerExists = true;
                    }
                    break;
                case "k7":
                    K7_Player k7Player = adminMapper.find_k7PlayerByNum(playerNum);
                    if (k7Player != null) {
                        adminMapper.deleteK7playerByPlayerNum(Collections.singletonList(k7Player.getK7PlayerNum()));
                        playerExists = true;
                    }
                    break;
                case "w1":
                    W1_Player wPlayer = adminMapper.find_w1PlayerByNum(playerNum);
                    if (wPlayer != null) {
                        adminMapper.deleteW1playerByPlayerNum(Collections.singletonList(wPlayer.getW1PlayerNum()));
                        playerExists = true;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("잘못된 playerType 입니다. playerType: " + playerType);
            }

        }

        // 모든 playerNums 처리 후, playerExists가 false라면 예외 발생
        if (!playerExists) {
            throw new IllegalArgumentException("해당 선수 번호에 해당하는 선수가 존재하지 않습니다. playerNums: " + playerNums);
        }
    }

    public void updatePlayer(Map<String, Object> paramMap, String selectedPlayerType) {
        System.out.println("서비스 -------- paramMap = " + paramMap);
        switch (selectedPlayerType.toLowerCase()) {
            case "k5":
                K5_Player k5_player = createK5Player(paramMap); // paramMap을 사용하여 K5_Player 객체 생성
                adminMapper.updateK5Player(k5_player); // K5_Player 정보를 업데이트하는 메서드 호출
                break;
            case "k7":
                K7_Player k7_player = createK7Player(paramMap); // paramMap을 사용하여 K7_Player 객체 생성
                adminMapper.updateK7Player(k7_player); // K7_Player 정보를 업데이트하는 메서드 호출
                break;
            case "w1":
                W1_Player w1_player = createW1Player(paramMap); // paramMap을 사용하여 W1_Player 객체 생성
                adminMapper.updateW1Player(w1_player); // W1_Player 정보를 업데이트하는 메서드 호출
                break;
            default:
                throw new IllegalArgumentException("유효하지 않은 선수 유형입니다: " + selectedPlayerType);
        }
    }


    public List<File> findFilesByK5PlayerNum(int k5PlayerNum) {
        return adminMapper.findFilesByK5PlayerNum(k5PlayerNum);
    }

    public List<File> findFilesByK7PlayerNum(int k7PlayerNum) {
        return adminMapper.findFilesByK7PlayerNum(k7PlayerNum);
    }

    public List<File> findFilesByW1PlayerNum(int w1PlayerNum) {
        return adminMapper.findFilesByW1PlayerNum(w1PlayerNum);
    }

    public boolean existsK5PlayerByNum(int playerNum) {
        return adminMapper.existsK5PlayerByNum(playerNum);
    }

    public boolean existsK7PlayerByNum(int playerNum) {
        return adminMapper.existsK7PlayerByNum(playerNum);

    }

    public boolean existsW1PlayerByNum(int playerNum) {
        return adminMapper.existsW1PlayerByNum(playerNum);

    }



// ================================== Player and


// ================================== Staff start

    public List<TeamStaff> getTeamStaff() {
        return adminMapper.getTeamStaff();
    }

    public void teamStaffSave(TeamStaff teamStaff) {
        adminMapper.insertTeamStaff(teamStaff);
    }

    public void teamStaffDelete(List<Integer> teamStaffNum) {

        for (int StaffNum : teamStaffNum) {
            TeamStaff teamStaff = adminMapper.findTeamStaffByNum(StaffNum);
            if (teamStaff == null) {
                throw new IllegalArgumentException("해당 StaffNum가 존재하지 않습니다. teamStaff: " + teamStaff);
            }
        }

        //찾은 글을 삭제
        adminMapper.deleteTeamStaff(teamStaffNum);
    }

    public TeamStaff findTeamStaffByNum(int teamStaffNum) {
        return adminMapper.findTeamStaffByNum(teamStaffNum);
    }

    public List<File> findFilesByTeamStaffNum(int teamStaffNum) {
        return adminMapper.findFilesByTeamStaffNum(teamStaffNum);
    }

    public void updateTeamStaff(TeamStaff teamStaff) {
        adminMapper.updateTeamStaff(teamStaff);
    }


// ================================== Staff and


// ================================== Rule start

    public List<Rule> getAllRule() {
        return adminMapper.getAllRule();
    }

    public Rule findRuleById(int ruleNum) {
        return adminMapper.findRuleById(ruleNum);
    }

    public List<File> findFilesByRuleNum(int ruleNum) {
        return adminMapper.findFilesByRuleNum(ruleNum);
    }


    public void updateRule(Rule rule) {
        rule.setRuleDate(getCurrentFormattedTime());
        adminMapper.updateRule(rule);
    }


    public void ruleDelete(List<Integer> ruleNums) {
        //데이터베이스에서 해당 ruleNums 의 rule 들을 찾는다.
        for (int ruleNum : ruleNums) {
            Rule rule = adminMapper.findRuleById(ruleNum);
            if (rule == null) {
                throw new IllegalArgumentException("해당 rule가 존재하지 않습니다. ruleNum: " + ruleNum);
            }
        }
        System.out.println("admin서비스 =========== ruleNums = " + ruleNums);
        //찾은 rule 글을 삭제
        adminMapper.deleteRule(ruleNums);
    }

    public int saveRule(Rule rule) {
        rule.setRuleDate(getCurrentFormattedTime());
        adminMapper.insertRule(rule);
        return rule.getRuleNum(); // MyBatis의 selectKey를 사용하여 생성된 ruleNum 반환
    }

    public void increaseRuleHits(int ruleNum) throws NotFoundException {
        Rule rule = adminMapper.findRuleByRuleNum(ruleNum);
        if (rule == null) {
            throw new NotFoundException(ruleNum + "번 규정을 찾을 수 없습니다.");
        }
        rule.setRuleHits(rule.getRuleHits() + 1);
        adminMapper.updateRuleHits(rule.getRuleNum(), rule.getRuleHits());
    }


// ================================== Rule and


    // ================================== 경영공시 start
    public List<Operation> getAllOperation() {
        return adminMapper.getAllOperation();
    }

    public Operation findOperationById(int operationNum) {
        return adminMapper.findOperationById(operationNum);
    }

    public List<File> findFilesByOperationNum(int operationNum) {
        return adminMapper.findFilesByOperationNum(operationNum);
    }

    public void updateOperation(Operation operation) {
        operation.setOperationDate(getCurrentFormattedTime());
        adminMapper.updateOperation(operation);
    }

    public void operationDelete(List<Integer> operationNums) {
        for (int operationNum : operationNums) {
            Operation operation = adminMapper.findOperationById(operationNum);
            if (operation == null) {
                throw new IllegalArgumentException("해당 operation가 존재하지 않습니다. operationNum: " + operationNum);
            }
        }
        adminMapper.deleteOperation(operationNums);
    }

    public int saveOperation(Operation operation) {
        operation.setOperationDate(getCurrentFormattedTime());
        adminMapper.insertOperation(operation);
        return operation.getOperationNum();
    }

    public void increaseOperationHits(int operationNum) throws NotFoundException {
        Operation operation = adminMapper.findOperationByOperationNum(operationNum);
        if (operation == null) {
            throw new NotFoundException(operationNum + "번 공시를 찾을 수 없습니다.");
        }
        operation.setOperationHits(operation.getOperationHits() + 1);
        adminMapper.updateOperationHits(operation.getOperationNum(), operation.getOperationHits());
    }


// ================================== 경영공시 and


// ================================== Faq start

    public List<Faq> getAllFaq() {
        return adminMapper.getAllFaq();
    }

    public Faq findFaqById(int faqId) {
        return adminMapper.findFaqById(faqId);
    }

    public void saveFaq(Faq faq) {
        adminMapper.insertFaq(faq);
    }

    public void updateFaq(Faq faq) {
        adminMapper.updateFaq(faq);
    }

    public void faqDelete(List<Integer> faqIds) {
        for (int faqId : faqIds) {
            Faq faq = adminMapper.findFaqById(faqId);
            if (faq == null) {
                throw new IllegalArgumentException("해당 Faq가 존재하지 않습니다. faqId: " + faqId);
            }
        }
        adminMapper.deleteFaq(faqIds);
    }


// ================================== Faq and

// ================================== 훈련일정 start


    public List<TrainingSchedule> getAllTrainingSchedule() {
        return adminMapper.getAllTrainingSchedule();
    }

    public int saveTrainingSchedule(TrainingSchedule trainingSchedule) {
        trainingSchedule.setScheduleDate(getCurrentFormattedTime());
        adminMapper.insertTrainingSchedule(trainingSchedule);
        return trainingSchedule.getScheduleNum();
    }

    public void increaseTrainingSchedule(int scheduleNum) throws NotFoundException {
        TrainingSchedule trainingSchedule = adminMapper.findScheduleByScheduleNum(scheduleNum);
        if (trainingSchedule == null) {
            throw new NotFoundException(scheduleNum + "번 일정을 찾을 수 없습니다");
        }
        trainingSchedule.setScheduleHits(trainingSchedule.getScheduleHits() + 1);
        adminMapper.updateTrainingScheduleHits(trainingSchedule.getScheduleNum(), trainingSchedule.getScheduleHits());
    }

    public TrainingSchedule findTrainingScheduleByNum(int scheduleNum) {
        return adminMapper.findScheduleByScheduleNum(scheduleNum);
    }

    public void updateTrainingSchedule(TrainingSchedule trainingSchedule) {
        trainingSchedule.setScheduleDate(getCurrentFormattedTime());
        adminMapper.updateTrainingSchedule(trainingSchedule);
    }

    public void trainingScheduleDelete(List<Integer> scheduleNums) {
        for (int scheduleNum : scheduleNums) {
            TrainingSchedule trainingSchedule = adminMapper.findScheduleByScheduleNum(scheduleNum);
            if (trainingSchedule == null) {
                throw new IllegalArgumentException("해당 scheduleNum가 존재하지 않습니다. scheduleNum: " + scheduleNum);
            }
        }
        adminMapper.deleteTrainingSchedule(scheduleNums);
    }


// ================================== 훈련일정 and


// ================================== 구단목록 start



    public List<Team> getAllTeamList() {
        return adminMapper.getAllTeamList();
    }

    public List<Team> findTeamsByLeague(String league) {
        return adminMapper.findTeamsByLeague(league);
    }

    public void registerTeam(Map<String, Object> paramMap) {
        adminMapper.registerTeam(paramMap);
    }

  /*  public Team findTeamByName(ing teamName) {
        return adminMapper.findTeamByName(teamName);
    }*/

    public Team findTeamById(int id) {
        return adminMapper.findTeamById(id);
    }


// ================================== 구단목록 and


// ================================== k5 구단목록 + 매치목록 start


    public List<Team> findK5TeamList(String teamName) {
        return adminMapper.findK5TeamList(teamName);
    }


    public List<Match> findK5MatchListByLeague(String league) {
        return adminMapper.findK5MatchListByLeague(league);
    }

    public void saveK5Match(Match matchRequest) {
        adminMapper.saveK5Match(matchRequest);
    }

    // 매치 정보 업데이트
    public void updateMatch(int matchId, Match matchDetails) {
        Match existingMatch = adminMapper.findMatchById(matchId);
        if (existingMatch == null) {
            throw new IllegalArgumentException("매치를 찾을 수 없습니다: ID " + matchId);
        }

        existingMatch.setTeamName(matchDetails.getTeamName());
        existingMatch.setMatchScore(matchDetails.getMatchScore());
        existingMatch.setMatchRelativeScore(matchDetails.getMatchRelativeScore());
        existingMatch.setMatchDate(matchDetails.getMatchDate());
        existingMatch.setMatchTime(matchDetails.getMatchTime());
        existingMatch.setMatchLocation(matchDetails.getMatchLocation());
        existingMatch.setMatchStatus(matchDetails.getMatchStatus());
        existingMatch.setMatchHome(matchDetails.getMatchHome());

        adminMapper.updateMatch(existingMatch);
    }

    public boolean deleteMatch(int matchId) {
        // 데이터베이스에서 매치 ID로 매치를 찾습니다.
        Match matchToDelete = adminMapper.findMatchById(matchId);

        if (matchToDelete == null) {
            // 매치를 찾을 수 없으면 예외를 던지는 대신 false를 반환합니다.
            return false;
        }

        // 매치를 찾았으므로 삭제를 진행합니다.
        adminMapper.deleteMatch(matchId);
        return true;
    }



// ================================== k5 구단목록 + 매치목록 and



// ================================== k7 구단목록 + 매치목록 start

    public List<Team> findK7TeamList(String teamName) {
        return adminMapper.findK7TeamList(teamName);
    }

    public List<Match> findK7MatchListByLeague(String league) {
        return adminMapper.findK7MatchListByLeague(league);
    }

    public void saveK7Match(Match matchRequest) {
        adminMapper.saveK7Match(matchRequest);
    }


// ================================== k7 구단목록 + 매치목록 and


// ================================== k7 구단목록 + 매치목록 start

    public List<Team> findW1TeamList(String teamName) {
        return adminMapper.findW1TeamList(teamName);
    }

    public List<Match> findW1MatchListByLeague(String league) {
        return adminMapper.findW1MatchListByLeague(league);

    }

    public void saveW1Match(Match matchRequest) {
        adminMapper.saveW1Match(matchRequest);
    }




// ================================== k7 구단목록 + 매치목록 and


}


