package org.example.eoullimback.user_auth.user;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.enums.user.OAuthProvider;
import org.example.eoullimback._common.error.exception.Exception401;
import org.example.eoullimback.user_auth.user.dto.request.UserRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/kakao")
    public String kakaoCallback(@RequestParam(name = "code") String code, HttpSession session) {

        try {
            User user = userService.kakaoSocialLogin(code);
            session.setAttribute("sessionUser", user);
        } catch (Exception e) {
            log.error("소셜 로그인 실패: {}", e.getMessage(), e);
            throw new Exception401(ErrorCode.USER_NOT_FOUND);
        }

//        return "redirect:/main/main";
        return "redirect:/admin/place";
    }

    // // http://localhost:8080/users/profile/1
    // 유저 컨트롤러 , @PathVariable Long id,
    @GetMapping("/profile")
    public String myProfile(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        User user = userService.getMyProfile(sessionUser.getId());

        boolean isKakaoUser = user.getProvider().equals(OAuthProvider.KAKAO);

        model.addAttribute("user", user);
        model.addAttribute("phone", user.getPhone() != null ? user.getPhone() : "");
        model.addAttribute("isKakaoUser", isKakaoUser);

        return "user/profile";
    }

    @GetMapping("/profile/edit")
    public String editProfile(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        User user = userService.getMyProfile(sessionUser.getId());

        model.addAttribute("sessionUser", sessionUser);
        model.addAttribute("user", user);

        return "user/update-profile";
    }

    /**
     * 회원 수정(프로필)
     */
    // // http://localhost:8080/users/profile/1
    @PostMapping("/profile")
    public String updateProfile(HttpSession session,
                                @ModelAttribute UserRequest.UpDateDTO update
    ) {
        update.validate();

        User sessionUser = (User) session.getAttribute("sessionUser");
        User updateUser = userService.updateProfile(sessionUser.getId(), update);

        session.setAttribute("sessionUser", updateUser);

        return "redirect:/user/profile";
    }

    /**
     * 회원 수정(프로필 이미지 삭제)
     */
    // // http://localhost:8080/users/profile-delete/1
    @PostMapping("/profile-delete")
    public String deleteProfileImage(HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        userService.deleteProfileImage(sessionUser.getId());

        User updateUser = userService.getMyProfile(sessionUser.getId());
        session.setAttribute("sessionUser", updateUser);

        return "redirect:/user/profile";
    }

    /**
     * 회원 탈퇴
     */
    // // http://localhost:8080/users/leave/1
    @PostMapping("/leave")
    public String leaveUser(HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");
        userService.leaveUser(sessionUser.getId());

        session.invalidate();

        return "redirect:/auth/login";
    }

    /**
     * 화면 : 사용자 비밀번호 체크
     */
    @GetMapping("/verify-password")
    public String verifyPassword(HttpSession session) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        if (sessionUser.getProvider() == OAuthProvider.KAKAO) {
            return "redirect:/user/profile";
        }

        return "/user/verify-password";
    }

    @GetMapping("/bookings")
    public String bookings(HttpSession session, Model model) {
        User sessionUser = (User) session.getAttribute("sessionUser");

        if (sessionUser != null) {
            model.addAttribute("user", sessionUser);
        }

        return "/user/booking";
    }

}
