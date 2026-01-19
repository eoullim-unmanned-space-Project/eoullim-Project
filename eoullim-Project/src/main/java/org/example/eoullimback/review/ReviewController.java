package org.example.eoullimback.review;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception409;
import org.example.eoullimback.review.dto.ReviewRequest;
import org.example.eoullimback.review.dto.ReviewResponse;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/rooms/{roomId}/reviews/new")
    public String createForm(@RequestParam Long placeId,
                             @PathVariable Long roomId,
                             @RequestParam Long paymentId,
                             Model model) {

        if (reviewService.existsByPaymentId(paymentId)) {
            throw new Exception409(ErrorCode.REVIEW_CONFLICT);
        }
        model.addAttribute("placeId", placeId);
        model.addAttribute("roomId", roomId);
        model.addAttribute("paymentId", paymentId);
        model.addAttribute("review", new ReviewRequest.CreateDTO());
        return "review/create-form";
    }

    @PostMapping("/rooms/{roomId}/reviews")
    public String create(HttpSession session,
                         @PathVariable Long roomId,
                         @RequestParam Long placeId,
                         @ModelAttribute("review") @Valid ReviewRequest.CreateDTO request,
                         BindingResult bindingResult,
                         Model model
    ) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        Long userId = (sessionUser != null) ? sessionUser.getId() : null;

        if (bindingResult.hasErrors()) {
            model.addAttribute("roomId", roomId);
            model.addAttribute("paymentId", request.getPaymentId());
            return "review/create-form";
        }
        reviewService.create(userId, roomId, request);

        return "redirect:/place/" + placeId + "/room";
    }

    @GetMapping("/reviews/{reviewId}/update")
    public String updateForm(@RequestParam Long placeId,
                             @PathVariable Long reviewId,
                             Model model
    ) {
        Review review = reviewService.findEntity(reviewId);
        model.addAttribute("review", new ReviewResponse.UpdateFormDTO(review));
        model.addAttribute("placeId", placeId);
        return "review/update-form";
    }

    @PostMapping("/rooms/reviews/{reviewId}")
    public String update(HttpSession session,
                         @RequestParam Long placeId,
                         @PathVariable Long reviewId,
                         @ModelAttribute("review") @Valid ReviewRequest.UpdateDTO request,
                         BindingResult bindingResult,
                         Model model
    ) {
        Review review = reviewService.findEntity(reviewId);
        if (bindingResult.hasErrors()) {
            model.addAttribute("placeId", placeId);
            model.addAttribute("review", new ReviewResponse.UpdateFormDTO(review));
            return "review/update-form";
        }

        User sessionUser = (User) session.getAttribute("sessionUser");
        Long userId = (sessionUser != null) ? sessionUser.getId() : null;

        reviewService.update(userId, reviewId, request);

        return "redirect:/place/" + placeId + "/room";
    }

    @PostMapping("/rooms/reviews/{reviewId}/delete")
    public String delete(HttpSession session,
                         @RequestParam Long placeId,
                         @PathVariable Long reviewId) {

        User sessionUser = (User) session.getAttribute("sessionUser");
        Long userId = (sessionUser != null) ? sessionUser.getId() : null;
        reviewService.delete(userId, reviewId);

        return "redirect:/place/" + placeId + "/room";
    }

    // 마이페이지 내 리뷰 리스트 확인
    @GetMapping("/my/reviews")
    public String page(HttpSession session, Model model) {

        User sessionUser = (User) session.getAttribute("sessionUser");

        model.addAttribute("user", sessionUser);

        return "user/review";
    }
}
