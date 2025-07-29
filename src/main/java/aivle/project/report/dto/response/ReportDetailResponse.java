package aivle.project.report.dto.response;

import aivle.project.report.domain.Report;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    private LocalDate createAt;
    private Long workerId;
    private String status;

    public static ReportDetailResponse fromEntity(Report report) {
        return ReportDetailResponse.builder()
                .reportId(report.getReportId())
                .carId(report.getCarId())
                .inspectionId(report.getInspectionId())
                .content(report.getContent())
                .createAt(report.getCreateAt())
                .workerId(report.getWorkerId())
                .status(report.getStatus().name())
                .build();
    }
}

