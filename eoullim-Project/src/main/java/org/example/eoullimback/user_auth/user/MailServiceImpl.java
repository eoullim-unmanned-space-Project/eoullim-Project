package org.example.eoullimback.user_auth.user;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception500;
import org.example.eoullimback._common.util.MailUtils;
import org.springframework.mail.MailException;
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

        } catch (MailException e) {
            throw new Exception500(ErrorCode.MAIL_SEND_FAIL);
        } catch (Exception e) {
            throw new Exception500(ErrorCode.MAIL_SEND_FAIL);
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
    public void sendLoginId(String email, String loginId) {

        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(email);
            helper.setSubject("[Eoullim] 아이디 안내 메일");
            helper.setText(
                    "<p>회원님의 아이디는</p> <h3>" + loginId + "</h3><p>입니다.</p>", true
            );

            javaMailSender.send(message);

        } catch (Exception e) {
            throw new Exception500(ErrorCode.MAIL_SEND_FAIL);
        }
    }

    @Override
    public void sendPasswordResetLink(String email, String resetLink) {

        System.out.println("[Mail] to : " + email);
        System.out.println("비밀번호 재설정 링크: " + resetLink);

    }
}
