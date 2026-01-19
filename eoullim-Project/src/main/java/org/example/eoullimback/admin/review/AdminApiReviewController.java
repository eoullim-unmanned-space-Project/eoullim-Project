package org.example.eoullimback.admin.review;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception409;
import org.example.eoullimback.review.ReviewRepository;
import org.example.eoullimback.review.ReviewService;
import org.example.eoullimback.review.dto.AdminReviewListResponse;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class AdminApiReviewController {

    private final ReviewService reviewService;
    private final ReviewRepository reviewRepository;

    @GetMapping("/api/admin/reviews")
    public List<AdminReviewListResponse> list(HttpSession session,
                                              @RequestParam(defaultValue = "") String keyword) {

        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new Exception409(ErrorCode.USER_NOT_FOUND);
//        if (!sessionUser.isAdmin()) throw new Exception409(ErrorCode.FORBIDDEN);

        return reviewService.findAll(keyword);
    }

    @DeleteMapping("/api/admin/reviews/{reviewId}")
    public ResponseEntity<?> delete(HttpSession session, @PathVariable Long reviewId) {

        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new Exception409(ErrorCode.USER_NOT_FOUND);
//        if (!sessionUser.isAdmin()) throw new Exception409(ErrorCode.FORBIDDEN);

        reviewService.adminDelete(sessionUser.getId(), reviewId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/admin/reviews/avg-rating")
    public Map<String, Object> avgRating() {
        Double avg = reviewRepository.findAverageRating();
        double safeAvg = (avg == null) ? 0.0 : avg;

        return Map.of(
                "avgRating", safeAvg
        );
    }
}
