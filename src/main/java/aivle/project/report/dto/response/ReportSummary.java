package aivle.project.report.dto.response;

import aivle.project.report.domain.Report;
import aivle.project.report.domain.enumerate.InspectionType;
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
    private String workerName;     // 회원 부분과 연결 필요
    private InspectionType type;  // enum으로 임의 저장. 나중에 연결 필요
    private Long workerId;
    private LocalDate createdAt;

    public static ReportSummary fromEntity(Report report) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return ReportSummary.builder()
                .reportId(report.getReportId())
                .auditId(report.getAuditId())
                //.workername(report.getWorkername())
                .type(report.getType())
                .workerId(report.getWorkerId())
                .createdAt(LocalDate.from(report.getCreatedAt()))
                .build();
    }
}
