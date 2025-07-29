package aivle.project.report.service;

import aivle.project.report.domain.Report;
import aivle.project.report.dto.response.ReportDetailResponse;
import aivle.project.report.dto.response.ReportListResponse;
import aivle.project.report.dto.response.ReportSummary;
import aivle.project.report.exception.ReportNotFoundException;
import aivle.project.report.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    public ReportDetailResponse findReportById(Long reportId) {
        if (reportId == null || reportId <= 0) {
            throw new IllegalArgumentException("Invalid report ID");
        }

        Report report = reportRepository.findById(reportId)
                .orElseThrow(ReportNotFoundException::new);

        return ReportDetailResponse.fromEntity(report);
    }

    public ReportListResponse findAllReports(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createAt"));
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

