package com.project.together.config.filter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.together.common.codes.ErrorCode;
import com.project.together.config.exception.BusinessExceptionHandler;
import com.project.together.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 아이디와 비밀번호 기반의 데이터를 Form 데이터로 전송을 받아 '인증'을 담당하는 필터입니다.
 */
@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }


    /**
     * 지정된 URL로 form 전송을 하였을 경우 파라미터 정보를 가져온다.
     *
     * @param request  from which to extract parameters and perform the authentication
     * @param response the response, which may be needed if the implementation has to do a
     *                 redirect as part of a multi-stage authentication process (such as OpenID).
     * @return Authentication {}
     * @throws AuthenticationException {}
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        UsernamePasswordAuthenticationToken authRequest;
        try {
            authRequest = getAuthRequest(request);
            System.out.println("로그인 시도중 authRequest = " + authRequest);
            setDetails(request, authRequest);
            return this.getAuthenticationManager().authenticate(authRequest);
        } catch (Exception e) {
            throw new BusinessExceptionHandler(e.getMessage(), ErrorCode.BUSINESS_EXCEPTION_ERROR);
        }

    }


    /**
     * Request로 받은 ID와 패스워드 기반으로 토큰을 발급한다.
     *
     * @param request HttpServletRequest
     * @return UsernamePasswordAuthenticationToken
     * @throws Exception e
     */
    private UsernamePasswordAuthenticationToken getAuthRequest(HttpServletRequest request) throws Exception {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
            Member member = objectMapper.readValue(request.getInputStream(), Member.class);
            log.debug("1.CustomAuthenticationFilter :: userId:" + member.getMember_id() + " userPw:" + member.getMember_pw());

            // ID와 패스워드를 기반으로 토큰 발급
            return new UsernamePasswordAuthenticationToken(member.getMember_id(), member.getMember_pw());
        } catch (UsernameNotFoundException ae) {
            throw new UsernameNotFoundException(ae.getMessage());
        } catch (Exception e) {
            throw new BusinessExceptionHandler(e.getMessage(), ErrorCode.IO_ERROR);
        }

    }
}
