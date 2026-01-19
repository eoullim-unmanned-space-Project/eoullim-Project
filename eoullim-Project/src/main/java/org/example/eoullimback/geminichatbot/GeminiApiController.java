package org.example.eoullimback.geminichatbot;

import lombok.RequiredArgsConstructor;
import org.example.eoullimback._common.enums.geminichatbot.Intent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class GeminiApiController {

    private final GeminiService geminiService;

    @PostMapping(value = "/chat", produces = "text/plain; charset=UTF-8")
    public Mono<String> chat(@RequestBody Map<String, String> req) {
        String input = req.getOrDefault("text", "").trim();

        if (input.isEmpty()) return Mono.just("질문을 입력해주세요. EX) 공간안내/운영시간/위치/가격/예약/환불");

        Intent intent = RuleEngine.match(input);
        String canned = CannedResponses.respond(intent);
        if (canned != null) return Mono.just(canned);

        return geminiService.simpleAnswer(input);
    }
}
