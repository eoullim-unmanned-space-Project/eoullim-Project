package org.example.eoullimback.review;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception409;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReviewApiController {

    private final ReviewQueryService reviewQueryService;

    @GetMapping("/api/user/reviews")
    public List<ReviewListItemDTO> list(HttpSession session,
                                        @RequestParam(defaultValue = "") String code,
                                        @RequestParam(defaultValue = "") String status) {

        User sessionUser = (User) session.getAttribute("sessionUser");
        if (sessionUser == null) throw new Exception409(ErrorCode.USER_NOT_FOUND);

        return reviewQueryService.findList(sessionUser.getId(), code, status);
    }
}
