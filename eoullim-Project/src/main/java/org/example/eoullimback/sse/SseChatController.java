package org.example.eoullimback.sse;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class SseChatController {

    private final SseChatService sseChatService;

    @GetMapping("/chat")
    public String index(Model model) {

        model.addAttribute("chatList", sseChatService.findAll());

        return "main/main";
    }

    @GetMapping(value = "/chat/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @ResponseBody
    public SseEmitter connect() {

        return sseChatService.createConnection(UUID.randomUUID().toString());
    }

    @PostMapping("/chat/send")
    @ResponseBody
    public void sendMessage(@RequestParam(name = "message") String message,
                              @RequestParam(name = "sender") String sender
    ) {
        sseChatService.addMessage(message, sender);
    }
}
