package com.project.together.service;

import com.project.together.domain.Qna;
import com.project.together.mapper.InfoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InfoService {

    private final InfoMapper infoMapper;

    public void saveQna(String qnaTitle, String qnaContent) {
        System.out.println("qnaTitle + \"qnaContent\" + qnaContent = " + qnaTitle + "qnaContent" + qnaContent);
        Qna qna = new Qna();
        qna.setQnaTitle(qnaTitle);
        qna.setQnaContent(qnaContent);
        //관리자 테이블의 외래키인 authId 컬럼을 어떻게 처리?
        //qnaDate 컬럼에 문의글 등록일 주입
        //qnaUpdate 컬럼에 수정일은 어떻게 처리?
        //qnaStatus 컬럼에 "답변대기"


        infoMapper.saveQna(qna);
    }
}
