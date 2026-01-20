package org.example.eoullimback._common.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.example.eoullimback.user_auth.user.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute
    public void addCsrfToken(Model model, HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

        if (csrfToken != null) {
            model.addAttribute("csrfToken", csrfToken.getToken());
            model.addAttribute("csrfHeader", csrfToken.getHeaderName());
        }
    }

    @ModelAttribute("sessionUser")
    public CustomUserDetails addSessionUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userDetails;
    }
}
