package org.example.eoullimback.user_auth.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.enums.user.OAuthProvider;
import org.example.eoullimback._common.error.exception.Exception401;
import org.example.eoullimback.user_auth.user.dto.request.UserRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
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
    @PreAuthorize("permitAll()")
    public String kakaoCallback(
            @RequestParam(name = "code") String code,
            HttpServletRequest request,
            HttpServletResponse response
 ) {

        try {
            User user = userService.kakaoSocialLogin(code);

            CustomUserDetails customUserDetails = new CustomUserDetails(user);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    customUserDetails,
                    null,
                    customUserDetails.getAuthorities()
            );

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(authentication);
            SecurityContextHolder.setContext(context);

            SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();

            securityContextRepository.saveContext(context, request, response);

            log.info("카카오 로그인 성공: {}", user.getEmail());

            HttpSession session = request.getSession(false);
            log.info("=== 카카오 로그인 완료 ===");
            log.info("사용자: {}", user.getEmail());
            log.info("세션 ID: {}", session != null ? session.getId() : "세션 없음");
            log.info("인증 객체: {}", authentication);
            log.info("권한: {}", authentication.getAuthorities());

        } catch (Exception e) {
            log.error("소셜 로그인 실패: {}", e.getMessage(), e);
            throw new Exception401(ErrorCode.USER_NOT_FOUND);
        }

        return "redirect:/public";
    }

    // // http://localhost:8080/users/profile/1
    // 유저 컨트롤러 , @PathVariable Long id,
    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public String myProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model
    ) {
        User sessionUser = userDetails.getUser();

        User user = userService.getMyProfile(sessionUser.getId());

        boolean isKakaoUser = user.getProvider().equals(OAuthProvider.KAKAO);

        model.addAttribute("user", user);
        model.addAttribute("phone", user.getPhone() != null ? user.getPhone() : "");
        model.addAttribute("isKakaoUser", isKakaoUser);

        return "user/profile";
    }

    /**
     * 회원 수정(프로필)
     */
    // // http://localhost:8080/users/profile/1
    @PostMapping("/profile")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public String updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @ModelAttribute UserRequest.UpDateDTO update,
            HttpSession session
    ) {
        update.validate();

        User user = userDetails.getUser();
        User updateUser = userService.updateProfile(user.getId(), update);

        session.setAttribute("sessionUser", updateUser);

        return "redirect:/user/profile";
    }

    @GetMapping("/profile/edit")
    @PreAuthorize("hasRole('USER')")
    public String editProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {

        User sessionUser = userDetails.getUser();
        User user = userService.getMyProfile(sessionUser.getId());

        model.addAttribute("sessionUser", sessionUser);
        model.addAttribute("user", user);

        return "user/update-profile";
    }

    /**
     * 회원 수정(프로필 이미지 삭제)
     */
    // // http://localhost:8080/users/profile-delete/1
    @PostMapping("/profile/image")
    @PreAuthorize("hasRole('USER')")
    public String deleteProfileImage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpSession session
    ) {
        User user = userDetails.getUser();
        userService.deleteProfileImage(user.getId());

        User updateUser = userService.getMyProfile(user.getId());
        session.setAttribute("sessionUser", updateUser);

        return "redirect:/user/profile";
    }

    /**
     * 회원 탈퇴
     */
    // // http://localhost:8080/users/leave/1
    @PostMapping("/profile/leave")
    @PreAuthorize("hasRole('USER')")
    public String leaveUser(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpSession session
            ) {
        User user = userDetails.getUser();
        userService.leaveUser(user.getId());

        session.invalidate();

        return "redirect:/auth/login";
    }

    /**
     * 화면 : 사용자 비밀번호 체크
     */
    @GetMapping("/profile/verify-password")
    @PreAuthorize("hasRole('USER')")
    public String verifyPassword(
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User user = userDetails.getUser();

        if (user.getProvider() == OAuthProvider.KAKAO) {
            return "redirect:/user/profile";
        }

        return "user/verify-password";
    }

    @GetMapping("/profile/bookings")
    @PreAuthorize("hasRole('USER')")
    public String bookings(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            Model model) {
        User user = userDetails.getUser();

        if (user != null) {
            model.addAttribute("user", user);
        }

        return "user/booking";
    }
}
