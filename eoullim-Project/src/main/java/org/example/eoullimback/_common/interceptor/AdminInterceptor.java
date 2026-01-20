package org.example.eoullimback._common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception401;
import org.example.eoullimback._common.error.exception.Exception403;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class AdminInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler
    ) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || auth instanceof AnonymousAuthenticationToken || !auth.isAuthenticated()) {
            throw new Exception401(ErrorCode.LOGIN_ONLY);
        }

        System.out.println("=== 인터셉터 권한 체크 ===");
        System.out.println("사용자 ID: " + auth.getName());

        // 2. 이 사용자가 들고 있는 실제 권한 리스트 (이게 [ROLE_ADMIN]이어야 함)
        System.out.println("보유 권한 리스트: " + auth.getAuthorities());


        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(ga -> ga.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            System.out.println("관리자 권한 없음! 403 예외 발생");
            throw new Exception403(ErrorCode.ACCESS_DENIED);
        }

        return true;
    }
}
