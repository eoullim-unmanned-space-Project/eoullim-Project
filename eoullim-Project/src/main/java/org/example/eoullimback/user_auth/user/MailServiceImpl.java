package org.example.eoullimback.user_auth.user;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.util.MailUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;
    private final HttpSession session;

    @Override
    public void sendVerificationCode(String email) {

        String code = MailUtils.generateRandomCode();

        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("[Eoullim] 회원가입 이메일 전송");
            helper.setText("<h3>인증번호는 [" + code +  "] 입니다.</h3>", true);

            javaMailSender.send(message);

            session.setAttribute("code_" + email, code);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean verifyVerificationCode(String email, String code) {

        String saveCode = (String) session.getAttribute("code_" + email);
        if (saveCode != null && saveCode.equals(code)) {
            session.removeAttribute("code_" + email);

            return true;
        }
        return false;
    }

    @Override
    public void paymentNotificationSender(String email, String message, String qrCode) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("[Eoullim] 결제 알림");
            helper.setText(
                            "<h2>결제 알림</h2>" +
                            "<p>" + message + "</p>" +
                            "<hr />" +
                            "<p><b>QR 코드</b></p>" +
                            "<p>" + qrCode + "</p>" +
                            "<p>현장에서 해당 QR 코드를 제시하여 주세요</p>", true);
            javaMailSender.send(mimeMessage);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
