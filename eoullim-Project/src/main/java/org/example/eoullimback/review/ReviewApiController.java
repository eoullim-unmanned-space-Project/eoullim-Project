package org.example.eoullimback.review;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception409;
import org.example.eoullimback.review.dto.ReviewListItemResponse;
import org.example.eoullimback.user_auth.user.CustomUserDetails;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewApiController {

    private final ReviewService reviewService;

    // 마이페이지 리뷰
    @GetMapping("/api/user/reviews")
    @PreAuthorize("hasRole('USER')")
    public List<ReviewListItemResponse> list(@AuthenticationPrincipal CustomUserDetails userDetails,
                                             @RequestParam(defaultValue = "") String code) {

        User user = userDetails.getUser();
        if (user == null) throw new Exception409(ErrorCode.USER_NOT_FOUND);

        return reviewService.findList(user.getId(), code);
    }

    // 마이페이지 리뷰 삭제
    @DeleteMapping("/api/user/reviews/{reviewId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> delete(@AuthenticationPrincipal CustomUserDetails userDetails,
                                    @PathVariable Long reviewId) {

        User user = userDetails.getUser();
        if (user == null) throw new Exception409(ErrorCode.USER_NOT_FOUND);

        reviewService.delete(user.getId(), reviewId);

        return ResponseEntity.ok().build();
    }
}