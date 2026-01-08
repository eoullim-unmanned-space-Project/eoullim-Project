package org.example.eoullimback.user_auth.user;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception500;
import org.example.eoullimback._common.util.MailUtils;
import org.springframework.beans.factory.annotation.Value;
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
            helper.setSubject("[Eoullim] 인증번호 이메일 전송");
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
//
//    @Override
//    public void sendPasswordResetLink(String email, String resetLink) {
//
//        System.out.println("[Mail] to : " + email);
//        System.out.println("비밀번호 재설정 링크: " + resetLink);
//
//    }




//    @Override
//    public void sendPaymentSuccessMail(Payment payment, byte[] qrImage) {
//
//        User user = (User) session.getAttribute("sessionUser");
//        if (user == null) {
//            throw new RuntimeException("로그인 유저 정보가 없습니다.");
//        }
//        String recipientEmail = user.getEmail();
//
//        MimeMessage message = javaMailSender.createMimeMessage();
//
//        try {
//            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//
//            helper.setFrom(fromEmail);
//            helper.setTo(recipientEmail);
//            helper.setSubject("[Eoullim] 결제 완료 - 무인 매장 입장권 안내");
//
//            String html = """
//                    <div>
//                        <h2>결제가 완료되었습니다.</h2>
//
//                        <p><strong>상품명:</strong> %s</p>
//                        <p><strong>결제 금액:</strong> %,d원</p>
//                        <p><strong>주문 번호:</strong> %s</p>
//
//                        <hr/>
//
//                        <h3>무인 매장 입장 QR 코드</h3>
//                        <p>아래 QR 코드를 매장 입구에서 스캔해 주세요.</p>
//
//                        <img src="cid:qrImage" width="250" height="250"/>
//
//                        <p style="margin-top:20px; color:#888;">
//                            ※ 본 QR 코드는 본인만 사용 가능하며 타인에게 공유할 수 없습니다.
//                        </p>
//                    </div>
//                    """.formatted(
//                            payment.getProductName(),
//                    payment.getAmount(),
//                    payment.getOrderId()
//            );
//
//            helper.setText(html, true);
//
//            helper.addInline(
//                    "qrImage",
//                    new ByteArrayResource(qrImage),
//                    "image/png"
//            );
//            javaMailSender.send(message);
//        } catch (Exception e) {
//            throw new Exception500(ErrorCode.MAIL_SEND_FAIL);
//        }
//    }
}
