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

    // refine 메서드 제거 (더 이상 사용 안 함)

    // 종합 요약: 검사 관련 모든 정보를 포함해서 요약
    public String summarize(String resolveContent, String aiSuggestion, String diagnosisResult, String inspectionType) {
        // 요약할 전체 내용 구성
        StringBuilder fullContent = new StringBuilder();

        fullContent.append("검사 타입: ").append(inspectionType != null ? inspectionType : "정보 없음").append("\n");

        if (diagnosisResult != null && !diagnosisResult.isEmpty()) {
            fullContent.append("진단 결과: ").append(diagnosisResult).append("\n");
        }

        if (aiSuggestion != null && !aiSuggestion.isEmpty()) {
            fullContent.append("AI 조치 제안: ").append(aiSuggestion).append("\n");
        }

        if (resolveContent != null && !resolveContent.isEmpty()) {
            fullContent.append("작업자 조치 내용: ").append(resolveContent).append("\n");
        }

        Map<String, Object> requestBody = Map.of(
                "model", "gpt-3.5-turbo",
                "messages", List.of(
                        Map.of("role", "system", "content",
                                "차량 검사 관련 정보들을 종합해서 5-7줄 정도로 요약해줘. " +
                                        "검사 결과, AI 제안사항, 실제 조치 내용을 포함해서 " +
                                        "핵심만 간단하고 가독성 있게 정리해줘."),
                        Map.of("role", "user", "content", fullContent.toString())
                ),
                "temperature", 0.5
        );

        return callGpt(requestBody);
    }

    // 단순 요약 (호환성을 위해 유지)
    public String summarize(String resolveContent) {
        return summarize(resolveContent, null, null, null);
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