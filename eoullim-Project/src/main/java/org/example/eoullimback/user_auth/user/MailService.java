package org.example.eoullimback.user_auth.user;

import org.example.eoullimback.payment.Payment;

public interface MailService {
    void sendVerificationCode(String email);
    boolean verifyVerificationCode(String email, String code);
    void sendPaymentSuccessMail(String email, Payment payment, byte[] qrImage);
    void sendPasswordResetLink(String email, String resetLink);
}
