package com.project.together.service;

import com.project.together.config.auth.PrincipalDetails;
import com.project.together.domain.Qna;
import com.project.together.domain.User;
import com.project.together.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.stereotype.Service;

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

    public List<Qna> getAllQnAs() {
        // 모든 QnA 정보를 가져옵니다.
        return adminMapper.getAllQnAs();
    }


    public Qna findById(int qnaNum) {
        return adminMapper.findById(qnaNum);
    }
}