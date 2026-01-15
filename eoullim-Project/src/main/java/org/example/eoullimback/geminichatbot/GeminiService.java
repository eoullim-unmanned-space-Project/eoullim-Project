package org.example.eoullimback.geminichatbot;

import reactor.core.publisher.Mono;

public interface GeminiService {
    Mono<String> simpleAnswer(String input);
}
