package org.example.eoullimback.admin.review;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception409;
import org.example.eoullimback.review.ReviewRepository;
import org.example.eoullimback.review.ReviewService;
import org.example.eoullimback.review.dto.AdminReviewListResponse;
import org.example.eoullimback.user_auth.user.CustomUserDetails;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AdminApiReviewController {

    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;

    // 관리자 리뷰 리스트 API
    @GetMapping("/api/admin/reviews")
    @PreAuthorize("hasRole('ADMIN')")
    public List<AdminReviewListResponse> list(@AuthenticationPrincipal CustomUserDetails userDetails,
                                              @RequestParam(defaultValue = "") String keyword) {

        User user = userDetails.getUser();
        if (user == null) throw new Exception409(ErrorCode.USER_NOT_FOUND);

        return reviewService.findAll(keyword);
    }

    // 관리자 리뷰 삭제
    @DeleteMapping("/api/admin/reviews/{reviewId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long reviewId) {

        User user = userDetails.getUser();
        if (user == null) throw new Exception409(ErrorCode.USER_NOT_FOUND);

        reviewService.adminDelete(user.getId(), reviewId);
        return ResponseEntity.ok().build();
    }

    // 관리자 리뷰 평균 별점
    @GetMapping("/api/admin/reviews/avg-rating")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, Object> avgRating() {
        Double avg = reviewRepository.findAverageRating();
        double safeAvg = (avg == null) ? 0.0 : avg;

        return Map.of(
                "avgRating", safeAvg
        );
    }
}
