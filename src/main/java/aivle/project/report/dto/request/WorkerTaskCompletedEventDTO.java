package aivle.project.report.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkerTaskCompletedEventDTO {
    private Long auditId;
    private String workerName;
    private Long inspectionId;
    private String resolve;
    private Long workerId;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private String type;
    private String aiSuggestion;      // AI 조치제안
    private String diagnosisResult;   // 검사결과
    private String resultDataPath;    // 결과데이터 사진 경로
}

