package aivle.project.report.dto.response;

import aivle.project.report.domain.Report;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

// 상세 조회
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReportDetailResponse {
    private Long reportId;
    private Long auditId;
    private Long inspectionId;
    private String resolve;
    private String summary;
    private LocalDateTime createdAt;
    private Long workerId;
    private String workerName;
    private String type;              // InspectionType
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private String aiSuggestion;      // AI 조치제안
    private String diagnosisResult;   // 검사결과
    private String resultDataPath;    // 결과데이터 사진 경로


    public static ReportDetailResponse fromEntity(Report report) {
        return ReportDetailResponse.builder()
                .reportId(report.getReportId())
                .auditId(report.getAuditId())
                .inspectionId(report.getInspectionId())
                .resolve(report.getResolve())          // 작업자 원본
                .summary(report.getSummary())
                .createdAt(report.getCreatedAt())
                .workerId(report.getWorkerId())
                .workerName(report.getWorkerName())
                .type(report.getType().name())
                .startedAt(report.getStartedAt())
                .endedAt(report.getEndedAt())
                .aiSuggestion(report.getAiSuggestion())    // 추가
                .diagnosisResult(report.getDiagnosisResult()) // 추가
                .resultDataPath(report.getResultDataPath())  // 추가
                .build();
    }
}

