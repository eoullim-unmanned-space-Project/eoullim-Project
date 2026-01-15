package org.example.eoullimback.geminichatbot;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GeminiChatBotController {

    @GetMapping(value="/chat", produces="text/plain; charset=UTF-8")
    public String chatGet() {
        return "main/main";
    }
}
