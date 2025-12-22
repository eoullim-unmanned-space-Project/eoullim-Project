package org.example.eoullimback.review;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback.review.dto.request.ReviewSaveRequest;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

//    @PostMapping
//    public String saveReview(
//            @Valid ReviewSaveRequest request,
//            HttpSession session
//    ) {
//        User sessionUser = (User) session.getAttribute("sessionUser");
//
//        if (sessionUser == null) {
//            throw new RuntimeException("로그인이 필요합니다.");
//        }
//
//        reviewService.saveReview(request, sessionUser);
//        return "redirect:/rooms" + request.roomId();
//    }
}
