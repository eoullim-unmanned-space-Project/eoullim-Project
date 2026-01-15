package org.example.eoullimback.geminichatbot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.eoullimback._common.enums.errors.ErrorCode;
import org.example.eoullimback._common.error.exception.Exception500;
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
@Slf4j
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

    @Override
    public String checkReviewContent(String content) {
        try {
            String formattedPrompt = contentFormatted(content);
            Map<String, Object> body = Map.of(
                    "contents", List.of(
                            Map.of("parts", List.of(Map.of("text", formattedPrompt))))
            );

            return webClient.post()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v1beta/models/gemini-2.5-flash:generateContent")
                            .queryParam("key", apiKey)
                            .build())
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Map.class)
                    .map(response -> {
                        List<Map<String, Object>> candidates = (List<Map<String, Object>>) response.get("candidates");
                        if (candidates == null || candidates.isEmpty()) return "ERROR";

                        Map<String, Object> firstCandidate = (Map<String, Object>) candidates.get(0);
                        Map<String, Object> aiContent = (Map<String, Object>) firstCandidate.get("content");
                        List<Map<String, Object>> parts = (List<Map<String, Object>>) aiContent.get("parts");

                        return parts.get(0).get("text").toString().trim();
                    })
                    .block();

        } catch (Exception e) {
            System.out.println("AI 서비스 통신 에러: " + e.getMessage());
            throw new Exception500(ErrorCode.GEMINI_AI_SERVICE_ERROR);
        }
}

    private String contentFormatted(String content) {

        String clearContent = content.trim().replace("\"", "'");

        return """
                당신은 리뷰 작성 관리자 챗봇입니다. 다음 규칙에 따라 판별해주세요." +
                "1. 욕설, 비하 발언, 정치적 혐오, 패드립, 인격모독, 타인에 대한 공격성 내용이 포함이 되어있다면 반드시 'BAD' 라고 답해줘." +
                "2. 위의 내용에 해당하지 않는 깨끗한 게시글이면 'OK'라고 답해줘." +
                "3. 설명은 생략하고 오직 'BAD' 또는 'OK' 단어 하나만 출력해줘." +
                "예시 1: '여기 사장님 진짜 장사 그지같이 하네요.' --> BAD(비하발언) " +
                "예시 2: '여기 사장님 엄청 친절하시네요.' --> OK" +
                "이제 사용자가 작성한 리뷰 글을 판별해줘. " +
                "사용자가 작성한 글 : %s
                """ + clearContent;
    }
}
