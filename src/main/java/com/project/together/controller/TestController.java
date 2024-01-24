package com.project.together.controller;


import com.project.together.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.project.together.config.auth.AuthConstants;
import com.project.together.config.jwt.TokenUtils;
import com.project.together.config.auth.ApiResponse;
import com.project.together.config.auth.SuccessCode;


@Slf4j
@RestController
@RequestMapping("api/v1/test")
public class TestController {
    /**
     * [API] 사용자 정보를 기반으로 JWT를 발급하는 API
     *
     * @param member Member
     * @return ApiResponseWrapper<ApiResponse> : 응답 결과 및 응답 코드 반환
     */
    @PostMapping("/generateToken")
    public ResponseEntity<ApiResponse> selectCodeList(@RequestBody Member member) {
        System.out.println("컨트롤러 member = " + member);
        String resultToken = TokenUtils.generateJwtToken(member);
        ApiResponse ar = ApiResponse.builder()
                // BEARER {토큰} 형태로 반환을 해줍니다.
                .result(AuthConstants.TOKEN_TYPE + " " + resultToken)
                .resultCode(SuccessCode.SELECT.getStatus())
                .resultMsg(SuccessCode.SELECT.getMessage())
                .build();

        return new ResponseEntity<>(ar, HttpStatus.OK);
    }
}

