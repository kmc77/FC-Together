package com.project.together.config.oauth;

import com.project.together.config.auth.PrincipalDetails;
import com.project.together.config.jwt.TokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println("OAuth2LoginSuccessHandler 입장 request = " + request);

        // 인증 객체에서 PrincipalDetails를 가져옵니다.
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        // 토큰을 생성합니다.
        String jwtToken = TokenUtils.createJwtToken(principalDetails);
        String refreshToken = TokenUtils.createRefreshToken(principalDetails);

        System.out.println("========= jwtToken = " + jwtToken);

        // 응답에 토큰을 추가합니다.
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);

        // HTML 응답으로 액세스 토큰 전달
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print("<!DOCTYPE html><html><body>");
        out.print("<script>");
        out.print("localStorage.setItem('accessToken', '" + jwtToken + "');");
        out.print("window.location.href = '/';"); // 메인 페이지로 리다이렉션
        out.print("</script>");
        out.print("</body></html>");
        out.flush();
    }
}
