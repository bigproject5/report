package aivle.project.report.dto.response;

import aivle.project.report.domain.Report;
import aivle.project.report.domain.enumerate.InspectionType;
import aivle.project.report.service.ReportService;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

// 전체 조회 시 나오는 요약 정보
@Getter
@Builder
public class ReportSummary {
    private Long reportId;
    private Long auditId;
    private String workerName;
    private InspectionType type;
    private Long workerId;
    private LocalDateTime createdAt;

    public static ReportSummary fromEntity(Report report) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String name = ReportService.maskName(report.getWorkerName());
        return ReportSummary.builder()
                .reportId(report.getReportId())
                .auditId(report.getAuditId())
                .workerName(name)
                .type(report.getType())
                .workerId(report.getWorkerId())
                .createdAt(report.getCreatedAt())
                .build();
    }
}
