package org.example.eoullimback.admin.inquiry;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback.inquiry.InquiryChatRoomService;
import org.example.eoullimback.inquiry.dto.response.InquiryChatRoomResponse;
import org.example.eoullimback.user_auth.user.CustomUserDetails;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminInquiryChatController {

    private final InquiryChatRoomService inquiryChatRoomService;

    @GetMapping("/admin/inquiries")
    @PreAuthorize("hasRole('ADMIN')")
    public String getListInquiries(Model model, @AuthenticationPrincipal CustomUserDetails userDetails) {

        User user = userDetails.getUser();

        List<InquiryChatRoomResponse.ListDTO> chatRoomList = inquiryChatRoomService.getAllInquiry();

        model.addAttribute("chatRoomList", chatRoomList);

        return "admin/inquiry/inquiry-list";
    }


}
