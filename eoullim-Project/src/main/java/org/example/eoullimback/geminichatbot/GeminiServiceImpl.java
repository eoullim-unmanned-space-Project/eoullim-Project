package org.example.eoullimback.geminichatbot;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GeminiServiceImpl implements GeminiService{

    private final WebClient webClient = WebClient.create("https://generativelanguage.googleapis.com");

    @Value("${GEMINI_API_KEY}")
    private String apiKey;

    @Override
    public Mono<String> simpleAnswer(String userInput) {

        String prompt = """
                당신은 고객지원 챗봇입니다. 답변은 한국어로 짧고 친절하게 해주세요.
                다음 범위 안에서만 답해주세요 : 공간, 운영시간, 위치, 가격, 예약, 환불/취소, 이용 방법
                키워드를 한번 더 물어보거나 비슷하게 말할 때에는 당신의 생각해서 상세하게 알려주세요
                
                범위 밖 질문이면 아래 문장 또는 생각해서 답해주세요.
   
                "제가 도와드릴 수 있는 건
                [공간안내/운영시간/위치/가격/예약/환불]에
                관한 내용입니다. 어떤걸 도와드릴까요?"
                
                사용자 질문: %s
                """.formatted(userInput);

        Map<String, Object> body = Map.of(
                "contents", List.of(
                        Map.of("parts", List.of(Map.of("text", prompt)))
                )
        );

        return webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1beta/models/gemini-2.5-flash:generateContent")
                        .queryParam("key", apiKey)
                        .build())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .map(resp -> {
                    var candidates = (List<Map<String, Object>>) resp.get("candidates");
                    if (candidates == null || candidates.isEmpty()) return "(응답 없음)";

                    var content = (Map<String, Object>) candidates.get(0).get("content");
                    if (content == null) return "(content 없음)";

                    var parts = (List<Map<String, Object>>) content.get("parts");
                    if (parts == null || parts.isEmpty()) return "(parts 없음)";

                    Object text = parts.get(0).get("text");
                    return text == null ? "(text 없음)" : text.toString();
                });
    }
}
