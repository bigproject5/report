package aivle.project.report.dto.response;

import aivle.project.report.domain.Report;
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
    private String carId;
    private String writer;     // 회원 부분과 연결 필요
    private Report.ReportType type;  // enum으로 임의 저장. 나중에 연결 필요
    private String companyNumber;   // 회원 부분과 연결 필요
    private LocalDateTime createdAt;

    public static ReportSummary fromEntity(Report report) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        return ReportSummary.builder()
                .reportId(report.getReportId())
                .carId(report.getCarId())
                //.writer(report.getWriter())
                .type(report.getType())
                //.companyNumber(report.getCompanyNumber())
                .createdAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                .build();
    }
}
