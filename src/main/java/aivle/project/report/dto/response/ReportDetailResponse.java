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
    private String carId;
    private Long inspectionId;
    private String content;
    private String summary;
    private LocalDateTime createdAt;
    private Long workerId;
    private String status;

    public static ReportDetailResponse fromEntity(Report report) {
        return ReportDetailResponse.builder()
                .reportId(report.getReportId())
                .carId(report.getCarId())
                .inspectionId(report.getInspectionId())
                .content(report.getContent())
                .summary(report.getSummary())
                .createdAt(report.getCreatedAt())
                .workerId(report.getWorkerId())
                .status(report.getStatus().name())
                .build();
    }
}

