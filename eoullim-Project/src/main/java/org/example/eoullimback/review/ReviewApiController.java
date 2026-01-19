package org.example.eoullimback.review;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception409;
import org.example.eoullimback.review.dto.ReviewListItemResponse;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewApiController {

    private final ReviewService reviewService;

    @GetMapping("/api/user/reviews")
    public List<ReviewListItemResponse> list(HttpSession session,
                                             @RequestParam(defaultValue = "") String code) {

        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new Exception409(ErrorCode.USER_NOT_FOUND);

        return reviewService.findList(sessionUser.getId(), code);
    }

    @DeleteMapping("/api/user/reviews/{reviewId}")
    public ResponseEntity<?> delete(HttpSession session,
                                    @PathVariable Long reviewId) {

        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new Exception409(ErrorCode.USER_NOT_FOUND);

        reviewService.delete(sessionUser.getId(), reviewId);

        return ResponseEntity.ok().build();
    }
}