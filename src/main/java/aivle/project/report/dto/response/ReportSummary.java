package aivle.project.report.dto.response;

import aivle.project.report.domain.Report;
import lombok.Builder;
import lombok.Getter;

// 전체 조회 시 나오는 요약 정보
@Getter
@Builder
public class ReportSummary {
    private Long reportId;
    private String carId;
    private String writer;     // 회원 부분과 연결 필요
    private String content;
    private String status;
    private String createdAt;

    public static ReportSummary fromEntity(Report report) {
        return ReportSummary.builder()
                .reportId(report.getReportId())
                .carId(report.getCarId())
                //.writer(report.getWriter())
                .content(report.getContent())
                .status(report.getStatus().name())
                .createdAt(report.getCreateAt().toString())
                .build();
    }
}
