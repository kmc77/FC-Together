package com.project.together.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {


    // 네이버 아이디는 전체 이메일 주소를 사용해야 합니다. 예: fctogether@naver.com
    @Value("${spring.mail.username}")
    private String id;

    // 네이버 계정의 비밀번호
    @Value("${spring.mail.password}")
    private String password;

    @Bean
    public JavaMailSender mailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.naver.com");  // SMTP 서버명
        javaMailSender.setUsername(id);  // 네이버 아이디
        javaMailSender.setPassword(password);  // 네이버 비밀번호
        javaMailSender.setPort(465);  // SMTP 포트

        javaMailSender.setJavaMailProperties(getMailProperties());
        javaMailSender.setDefaultEncoding("UTF-8");  // 메일 인코딩 설정

        return javaMailSender;
    }

    // 메일 인증서버 정보 가져오기
    private Properties getMailProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");  // 프로토콜 설정
        properties.setProperty("mail.smtp.auth", "true");  // SMTP 인증 필요
        properties.setProperty("mail.smtp.ssl.enable", "true");  // SSL 사용 설정
        properties.setProperty("mail.debug", "true");  // 디버그 모드 설정

        return properties;
    }
}
