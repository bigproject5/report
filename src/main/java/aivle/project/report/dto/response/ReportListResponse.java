package aivle.project.report.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

// 전체 응답 툴
@Getter
@Builder
public class ReportListResponse {
    private List<ReportSummary> reports;
    private int page;
    private int size;
    private long total;

    public static ReportListResponse of(List<ReportSummary> reports, int page, int size, long total) {
        return ReportListResponse.builder()
                .reports(reports)
                .page(page)
                .size(size)
                .total(total)
                .build();
    }
}

