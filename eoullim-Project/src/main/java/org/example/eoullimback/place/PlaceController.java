package org.example.eoullimback.place;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.dto.PageResponse;
import org.example.eoullimback.review.ReviewResponse;
import org.example.eoullimback.review.ReviewService;
import org.example.eoullimback.review.ReviewablePaymentDTO;
import org.example.eoullimback.room.RoomResponse;
import org.example.eoullimback.room.RoomService;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;
    private final RoomService roomService;
    private final ReviewService reviewService;


    // 전체 조회
    @GetMapping("/map")
    public String ListPlace(Model model,
                            @RequestParam(defaultValue = "1") int page,
                            @RequestParam(defaultValue = "8") int size,
                            @RequestParam(required = false) String keyword
    ) {
        int pageIndex = Math.max(0, page - 1);
        PageResponse.PageDTO<Place, PlaceResponse.ListDTO> placePage = placeService.placeList(pageIndex, size, keyword);
        model.addAttribute("placePage", placePage);
        model.addAttribute("placeKeyword", keyword != null ? keyword : "");

        return "/map/map";
    }

    // 새로운 장소 4개만 조회
    @GetMapping("/")
    public String newPlace(Model model
    ) {
        List<PlaceResponse.ListDTO> place = placeService.newPlace();
        model.addAttribute("place", place);
        // 리뷰 뷰
        List<ReviewResponse.ListDTO> reviews = reviewService.findLatestReviews(6);
        model.addAttribute("reviews", reviews);

        return "/main/main";
    }

    @GetMapping("/place/{placeId}/room")
    public String ListRoom(Model model,
                           @PathVariable Long placeId,
//                           @RequestParam(required = false) Long roomId,
                           HttpSession session
    ) {
        // 리뷰 작성 시 로그인
        User sessionUser = (User) session.getAttribute("sessionUser");
        Long userId = (sessionUser != null) ? sessionUser.getId() : null;
        List<RoomResponse.ListDTO> roomList = roomService.roomList(placeId);
        Place place = placeService.placeUpdateForm(placeId);
        model.addAttribute("roomList", roomList);
        model.addAttribute("place", place);
        // 리뷰 뷰
        if (!roomList.isEmpty()) {
            Long roomId = roomList.get(0).getRoomId();
            model.addAttribute("roomId", roomId);
            List<ReviewResponse.ListDTO> reviews = reviewService.findByRoom(userId, placeId, roomId);
            model.addAttribute("reviews", reviews);
            if (userId != null) {
                List<ReviewablePaymentDTO> reviewablePayments =
                        reviewService.findReviewablePayments(userId, roomId);
                model.addAttribute("reviewablePayments", reviewablePayments);
            }
        }
        return "room/list";
    }


//    @PostMapping("/place/{placeId}")
//    public String UpdateProc(@PathVariable Long placeId,
//                             PlaceRequest.UpdateDTO request
//    ) {
//        request.validate();
//
//        Place place = placeService.placeUpdate(placeId, request);
//
//        return "redirect:/admin/place";
//    }
}
