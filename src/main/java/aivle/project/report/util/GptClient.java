package aivle.project.report.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class GptClient {

    @Value("${openai.api.key}")
    private String apiKey;

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    private RestTemplate restTemplate = new RestTemplate();

    // 1차 GPT 호출: 중구난방 텍스트를 정돈된 문장으로
    public String refine(String rawContent) {
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content", "작업자가 중구난방으로 작성한 차량 조치 내용을 문법적으로 자연스럽고 논리적인 문장으로 정리해주세요."),
                        Map.of("role", "user", "content", rawContent)
                ),
                "temperature", 0.5
        );

        return callGpt(requestBody);
    }

    // 2차 GPT 호출: 정돈된 문장을 요약된 핵심 보고서로
    public String summarize(String refinedContent) {
        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content", "정리된 차량 조치 내용을 5줄 정도로 요약해줘. 핵심 이상 항목만 간단히 가독성 있게 정리해줘."),
                        Map.of("role", "user", "content", refinedContent)
                ),
                "temperature", 0.5
        );

        return callGpt(requestBody);
    }

    // GPT API 공통 호출 로직
    private String callGpt(Map<String, Object> requestBody) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(
                    API_URL,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class
            );

            Map choice = (Map) ((List) response.getBody().get("choices")).get(0);
            Map message = (Map) choice.get("message");
            return (String) message.get("content");

        } catch (Exception e) {
            log.error("GPT 호출 실패: {}", e.getMessage());
            return "GPT 처리 실패";
        }
    }
}