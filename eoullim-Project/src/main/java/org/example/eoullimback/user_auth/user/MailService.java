package org.example.eoullimback.user_auth.user;

public interface MailService {
    void sendVerificationCode(String email);
    boolean verifyVerificationCode(String email, String code);
//    void sendPasswordResetLink(String email, String resetLink);
}
