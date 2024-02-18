package com.project.together.service;

import com.project.together.domain.Qna;
import com.project.together.domain.User;
import com.project.together.mapper.MyMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyService {

    private final MyMapper myMapper;

    public void saveQna(String qnaTitle, String qnaContent, int userId, String username) {
        System.out.println("서비스 username = " + username);
        System.out.println("서비스 userId = " + userId);

        Qna qna = new Qna();
        qna.setQnaTitle(qnaTitle);
        qna.setQnaContent(qnaContent);
        qna.setUsername(username); // 현재 사용자의 아이디 설정
        qna.setId(userId); // 현재 사용자의 id 설정

        // qnaDate 컬럼에 문의글 등록일 주입
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        qna.setQnaDate(timestamp.toString());

        // qnaUpdate 컬럼에 수정일은 어떻게 처리?
        // qnaStatus 컬럼에 "답변대기" 설정
        qna.setQnaStatus("답변대기");

        myMapper.saveQna(qna);
    }

    //사용자 문의글 전체 조회
    public List<Qna> getQnaListByUserName(String username) {
        User user = myMapper.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username + " 사용자를 찾을 수 없습니다.");
        }
        return myMapper.findQnaByUsername(username);
    }

    //사용자 특정 문의글 조회
    public Qna getQna(int qnaNum) throws NotFoundException {
        Qna qna = myMapper.findQnaByQnaNum(qnaNum);
        if (qna == null) {
            throw new NotFoundException(qnaNum + " 번호의 문의를 찾을 수 없습니다.");
        }
        return qna;
    }


}
