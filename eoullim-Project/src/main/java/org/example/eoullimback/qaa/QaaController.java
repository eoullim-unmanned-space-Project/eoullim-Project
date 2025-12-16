package org.example.eoullimback.qaa;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.eoullimback.user_auth.user.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
@Controller
public class QaaController {

    private final QaaRepository qaaRepository;

    @PostMapping("/qaa")
    public String createQaa(HttpSession session) {
        User sesionUser = (User)session.getAttribute("sessionUser");

//        Qaa qaa = saveDto.toEntity(sessionUser);
//        qaaRepository.save(qaa);

        return "redirect:/qaa/list";
    }
}
