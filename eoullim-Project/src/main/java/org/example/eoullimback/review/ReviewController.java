package org.example.eoullimback.review;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception409;
import org.example.eoullimback.review.dto.ReviewRequest;
import org.example.eoullimback.review.dto.ReviewResponse;
import org.example.eoullimback.user_auth.user.CustomUserDetails;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 생성 화면
    @GetMapping("/rooms/{roomId}/reviews/new")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
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

    // 리뷰 생성
    @PostMapping("/rooms/{roomId}/reviews")
    @PreAuthorize("hasRole('USER')")
    public String create(@AuthenticationPrincipal CustomUserDetails userDetails,
                         @PathVariable Long roomId,
                         @RequestParam Long placeId,
                         @ModelAttribute("review") @Valid ReviewRequest.CreateDTO request,
                         BindingResult bindingResult,
                         Model model
    ) {
        User user = userDetails.getUser();
        Long userId = (user != null) ? user.getId() : null;

        if (bindingResult.hasErrors()) {
            model.addAttribute("roomId", roomId);
            model.addAttribute("paymentId", request.getPaymentId());
            return "review/create-form";
        }
        reviewService.create(userId, roomId, request);

        return "redirect:/place/" + placeId + "/room";
    }

    // 리뷰 수정 화면
    @GetMapping("/reviews/{reviewId}/update")
    @PreAuthorize("hasRole('USER')")
    public String updateForm(@RequestParam Long placeId,
                             @PathVariable Long reviewId,
                             Model model
    ) {
        Review review = reviewService.findEntity(reviewId);
        model.addAttribute("review", new ReviewResponse.UpdateFormDTO(review));
        model.addAttribute("placeId", placeId);
        return "review/update-form";
    }

    // 리뷰 수정
    @PostMapping("/reviews/{reviewId}")
    @PreAuthorize("hasRole('USER')")
    public String update(@AuthenticationPrincipal CustomUserDetails userDetails,
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

        User user = userDetails.getUser();
        Long userId = (user != null) ? user.getId() : null;

        reviewService.update(userId, reviewId, request);

        return "redirect:/place/" + placeId + "/room";
    }

    // 리뷰 삭제
    @PostMapping("/rooms/reviews/{reviewId}/delete")
    @PreAuthorize("hasRole('USER')")
    public String delete(@AuthenticationPrincipal CustomUserDetails userDetails,
                         @RequestParam Long placeId,
                         @PathVariable Long reviewId) {

        User user = userDetails.getUser();
        Long userId = (user != null) ? user.getId() : null;
        reviewService.delete(userId, reviewId);

        return "redirect:/place/" + placeId + "/room";
    }

    // 마이페이지 내 리뷰 리스트 확인
    @GetMapping("/user/reviews")
    @PreAuthorize("hasRole('USER')")
    public String page(@AuthenticationPrincipal CustomUserDetails userDetails,
                       Model model) {

        User user = userDetails.getUser();

        model.addAttribute("user", user);

        return "user/review";
    }
}
