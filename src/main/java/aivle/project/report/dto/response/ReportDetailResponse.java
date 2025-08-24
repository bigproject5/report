package aivle.project.report.dto.response;

import aivle.project.report.domain.Report;
import aivle.project.report.service.ReportService;
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
    private String type;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;

    public static ReportDetailResponse fromEntity(Report report) {
        String name = ReportService.maskName(report.getWorkerName());
        return ReportDetailResponse.builder()
                .reportId(report.getReportId())
                .auditId(report.getAuditId())
                .inspectionId(report.getInspectionId())
                .resolve(report.getResolve()) // 가공된 문장
                .summary(report.getSummary())
                .createdAt(report.getCreatedAt())
                .workerId(report.getWorkerId())
                .type(report.getType().name())
                .startedAt(report.getStartedAt())
                .workerName(name)
                .endedAt(report.getEndedAt())
                .build();
    }
}

