package aivle.project.report.service;

import aivle.project.report.domain.Report;
import aivle.project.report.domain.enumerate.InspectionType;
import aivle.project.report.dto.request.WorkerTaskCompletedEventDTO;
import aivle.project.report.dto.response.ReportDetailResponse;
import aivle.project.report.dto.response.ReportListResponse;
import aivle.project.report.dto.response.ReportSummary;
import aivle.project.report.exception.ReportNotFoundException;
import aivle.project.report.repository.ReportRepository;
import aivle.project.report.util.GptClient;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        if (report.getSummary() == null || report.getSummary().isBlank()) {
            String summary = gptClient.summarize(report.getResolve());
            report.setSummary(summary);
            reportRepository.save(report);
        }

        return ReportDetailResponse.fromEntity(report);
    }


    @Transactional
    public Report saveReportWithGpt(WorkerTaskCompletedEventDTO request) {
        String refinedContent = gptClient.refine(request.getResolve());
        String summary = gptClient.summarize(refinedContent);

        InspectionType type;
        try {
            type = InspectionType.valueOf(request.getType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효하지 않은 type 값입니다.");
        }
        Report report = Report.toEntity(request, refinedContent, summary, type);
        return reportRepository.save(report);
    }

    // 다시 요약
    @Transactional
    public String reSummarize(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(ReportNotFoundException::new);

        String newSummary = gptClient.summarize(report.getResolve());
        report.setSummary(newSummary);
        return newSummary;
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

    public static String maskName(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }
        int length = name.length();

        if (length == 1) {
            return name;
        } else if (length == 2) {
            return name.charAt(0) + "*";
        } else {
            String firstName = name.substring(0, 1);
            String lastName = name.substring(length - 1);
            String middleMask = "*".repeat(length - 2); // Java 11+

            return firstName + middleMask + lastName;
        }
    }
}

