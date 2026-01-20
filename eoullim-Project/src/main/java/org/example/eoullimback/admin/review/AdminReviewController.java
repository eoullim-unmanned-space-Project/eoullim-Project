package org.example.eoullimback.admin.review;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception409;
import org.example.eoullimback.user_auth.user.CustomUserDetails;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminReviewController {

    // 관리자 리뷰 리스트 화면
    @GetMapping("/admin/reviews")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public String page(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        if (user == null) throw new Exception409(ErrorCode.USER_NOT_FOUND);

        return "admin/review";
    }
}
