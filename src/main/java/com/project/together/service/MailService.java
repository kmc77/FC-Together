package com.project.together.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    public void sendVerificationMail(String to, int verificationCode) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("fctogether@naver.com");
        helper.setTo(to);
        helper.setSubject("FC Together 비밀번호 재설정 인증번호");
        helper.setText(buildHtmlContent(verificationCode), true);

        mailSender.send(message);
    }

    private String buildHtmlContent(int verificationCode) {
        return "<div style='font-family: Arial, sans-serif; margin: 20px; padding: 20px; border: 1px solid #EDEDED; box-shadow: 0 2px 3px rgba(0, 0, 0, 0.1); border-radius: 8px;'>" +
                "<h3 style='color: #333;'>비밀번호 재설정 인증번호</h3>" +
                "<div style='background-color: #F9F9F9; padding: 20px; border-radius: 5px;'>" +
                "<p>귀하의 비밀번호 재설정을 위한 인증번호는 " +
                "<strong style='display: inline-block; background-color: #FFFFFF; padding: 8px 12px; border: 1px solid #DDD; border-radius: 4px; color: #0056b3;'>" +
                verificationCode + "</strong> 입니다.</p>" +
                "</div>" +
                "<br><br><p style='font-size: small; color: #666;'>*이 메일은 시스템에 의해 자동 발송되었습니다. 답장을 보내지 마십시오.</p>" +
                "</div>";
    }
}
