package org.example.eoullimback.review;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/rooms/{roomId}/reviews")
    public String reviewList(
            @PathVariable Long roomId,
            @RequestParam(required = false) Byte rating,
            @RequestParam(defaultValue = "latest") String sort,
            Model model
    ) {
        List<ReviewResponse.ListDTO> reviews = reviewService.findByRoom(roomId, rating, sort);

        model.addAttribute("reviews", reviews);
        model.addAttribute("roomId", roomId);
        model.addAttribute("rating", rating);
        model.addAttribute("sort", sort);

        return "room/list";
    }

    @PostMapping("/rooms/{roomId}/reviews")
    public String create(
            @PathVariable Long roomId,
            @ModelAttribute("review") @Valid ReviewRequest.CreateDTO request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "review/create-form";
        }

        Long userId = getLoginUserId();
        reviewService.create(userId, roomId, request);

        return "redirect:/rooms/" + roomId + "/reviews";
    }

    @GetMapping("/reviews/{reviewId}/update")
    public String updateForm(
            @PathVariable Long reviewId,
            Model model
    ) {
        Review review = reviewService.findEntity(reviewId);
        model.addAttribute("review",new ReviewResponse.UpdateFormDTO(review));

        return "review/update-form";
    }

    @PostMapping("/rooms/reviews/{reviewId}")
    public String update(
            @PathVariable Long reviewId,
            @ModelAttribute("review") @Valid ReviewRequest.UpdateDTO request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return "review/update-form";
        }

        Long userId = getLoginUserId();
        reviewService.update(userId, reviewId, request);

        return "redirect:/rooms/";
    }

    @PostMapping("/rooms/reviews/{reviewId}/delete")
    public String delete(@PathVariable Long reviewId) {

        Long userId = getLoginUserId();
        reviewService.delete(userId, reviewId);

        return "redirect:/rooms/";
    }

    // TODO: HttpSession 교체 (더미 데이터)
    private Long getLoginUserId() {
        return 1L;
    }
}
