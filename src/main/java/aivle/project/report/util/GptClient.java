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
                                "다음 차량 검사 정보를 바탕으로 전문적인 검사 보고서를 작성해주세요. " +
                                        "보고서는 다음 형식을 따라 작성하되, 제공된 정보에 맞춰 내용을 구성해주세요:\n\n" +
                                        "**검사 개요**\n" +
                                        "- 검사 유형 및 목적 설명\n" +
                                        "- 검사 실시 배경\n\n" +
                                        "**검사 결과 분석**\n" +
                                        "- 주요 발견사항\n" +
                                        "- 문제점 및 이상 징후\n" +
                                        "- 안전성 평가\n\n" +
                                        "**조치 사항**\n" +
                                        "- AI 권장 조치사항 검토\n" +
                                        "- 실제 수행된 조치 내용\n" +
                                        "- 조치 효과 및 완료 상태\n\n" +
                                        "**종합 평가 및 권고사항**\n" +
                                        "- 전체적인 차량 상태 평가\n" +
                                        "- 향후 관리 방안\n" +
                                        "- 추가 점검 필요 사항\n\n" +
                                        "각 섹션별로 충분한 내용을 포함하여 전문적이고 체계적인 보고서로 작성해주세요. " +
                                        "문장은 정중하고 공식적인 어조를 사용하며, 구체적인 근거와 함께 설명해주세요."),
                        Map.of("role", "user", "content", fullContent.toString())
                ),
                "temperature", 0.3
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