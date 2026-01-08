package org.example.eoullimback.user_auth;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class FindPageController {


    @GetMapping("/api/find/login-id")
    public String findLoginId() {
        return "user/find-loginId";
    }

}
