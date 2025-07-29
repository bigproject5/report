package aivle.project.report.service;

import aivle.project.report.domain.Report;
import aivle.project.report.dto.response.ReportDetailResponse;
import aivle.project.report.dto.response.ReportListResponse;
import aivle.project.report.dto.response.ReportSummary;
import aivle.project.report.exception.ReportNotFoundException;
import aivle.project.report.repository.ReportRepository;
import aivle.project.report.util.GptClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;
    private final GptClient gptClient;

    public ReportDetailResponse findReportById(Long reportId) {
        if (reportId == null || reportId <= 0) {
            throw new IllegalArgumentException("Invalid report ID");
        }

        Report report = reportRepository.findById(reportId)
                .orElseThrow(ReportNotFoundException::new);

        String summary = gptClient.summarize(report.getContent());

        return ReportDetailResponse.builder()
                .reportId(report.getReportId())
                .carId(report.getCarId())
                .inspectionId(report.getInspectionId())
                .content(report.getContent())
                .summary(summary)
                .createdAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                .workerId(report.getWorkerId())
                .status(report.getStatus().name())
                .build();
    }

    public ReportListResponse findAllReports(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        var reportsPage = reportRepository.findAll(pageRequest)
                .map(ReportSummary::fromEntity);

        return ReportListResponse.builder()
                .reports(reportsPage.getContent())
                .page(reportsPage.getNumber() + 1)
                .size(reportsPage.getSize())
                .total(reportsPage.getTotalElements())
                .build();
    }
}

