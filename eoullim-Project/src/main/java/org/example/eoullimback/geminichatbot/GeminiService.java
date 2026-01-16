package org.example.eoullimback.geminichatbot;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import reactor.core.publisher.Mono;

public interface GeminiService {
    Mono<String> simpleAnswer(String input);
    String checkReviewContent(String content);
    String checkQaaTitleAndContent(String title, String content);
}
