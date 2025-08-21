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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final ReportRepository reportRepository;
    private final GptClient gptClient;

    public ReportDetailResponse findReportById(Long reportId) {
        if (reportId == null || reportId <= 0) {
            throw new IllegalArgumentException("Invalid report ID");
        }

        Report report = reportRepository.findById(reportId)
                .orElseThrow(ReportNotFoundException::new);

        // summary가 없는 경우 종합 요약 생성
        if (report.getSummary() == null || report.getSummary().isBlank()) {
            String summary = gptClient.summarize(
                    report.getResolve(),
                    report.getAiSuggestion(),
                    report.getDiagnosisResult(),
                    report.getType() != null ? report.getType().name() : null
            );
            report.setSummary(summary);
            reportRepository.save(report);
        }

        return ReportDetailResponse.fromEntity(report);
    }

    @Transactional
    public Report saveReportWithGpt(WorkerTaskCompletedEventDTO request) {
        try {
            // 종합 요약 생성 (정제 과정 제거)
            String summary = gptClient.summarize(
                    request.getResolve(),          // 작업자 조치내용
                    request.getAiSuggestion(),     // AI 조치제안
                    request.getDiagnosisResult(),  // 진단결과
                    request.getType()              // 검사타입
            );

            // GPT 실패 체크
            if ("GPT 처리 실패".equals(summary)) {
                log.warn("GPT 요약 실패 - 기본 메시지로 저장: auditId={}", request.getAuditId());
                summary = "AI 요약 실패 - 수동 확인 필요";
            }

            InspectionType type;
            try {
                type = InspectionType.valueOf(request.getType().toUpperCase());
            } catch (IllegalArgumentException e) {
                log.error("유효하지 않은 InspectionType: {}", request.getType());
                throw new IllegalArgumentException("유효하지 않은 type 값입니다: " + request.getType());
            }

            Report report = Report.builder()
                    .auditId(request.getAuditId())
                    .inspectionId(request.getInspectionId())
                    .workerId(request.getWorkerId())
                    .workerName(request.getWorkerName())
                    .resolve(request.getResolve())                    // 작업자 조치내용 (원본)
                    .summary(summary)                                 // 종합 요약
                    .aiSuggestion(request.getAiSuggestion())          // AI 조치제안
                    .diagnosisResult(request.getDiagnosisResult())    // 진단결과
                    .resultDataPath(request.getResultDataPath())     // 결과데이터 경로
                    .startedAt(request.getStartedAt())
                    .endedAt(request.getEndedAt())
                    .type(type)
                    .reportStatus(Report.ReportStatus.CREATED)        // 상태 설정
                    .build();

            Report savedReport = reportRepository.save(report);
            log.info("레포트 저장 완료: reportId={}, auditId={}", savedReport.getReportId(), savedReport.getAuditId());
            return savedReport;

        } catch (Exception e) {
            log.error("레포트 저장 중 오류: auditId={}, error={}", request.getAuditId(), e.getMessage(), e);
            throw e;
        }
    }

    // 다시 요약 - 종합 요약으로 수정
    @Transactional
    public String reSummarize(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(ReportNotFoundException::new);

        // 종합 정보로 다시 요약
        String newSummary = gptClient.summarize(
                report.getResolve(),
                report.getAiSuggestion(),
                report.getDiagnosisResult(),
                report.getType() != null ? report.getType().name() : null
        );

        report.setSummary(newSummary);
        reportRepository.save(report);

        log.info("레포트 재요약 완료: reportId={}", reportId);
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

    public List<ReportSummary> getWorkerReport(Long workerId) {
        List<Report> reports = reportRepository.findByWorkerId(workerId);
        return reports.stream().map(ReportSummary::fromEntity).toList();
    }
}