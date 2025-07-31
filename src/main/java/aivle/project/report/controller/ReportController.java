package aivle.project.report.controller;

import aivle.project.report.domain.Report;
import aivle.project.report.dto.request.ReportCreateRequest;
import aivle.project.report.dto.response.ReportDetailResponse;
import aivle.project.report.dto.response.ReportListResponse;
import aivle.project.report.dto.response.ReportSummary;
import aivle.project.report.dto.response.ReportSummaryRequest;
import aivle.project.report.repository.ReportRepository;
import aivle.project.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/taskreports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;
    private final ReportRepository reportRepository;

    // 레포트 상세 조회
    @GetMapping("/reports/{reportId}")
    public ResponseEntity<?> getReport(@PathVariable Long reportId) {
        ReportDetailResponse response = reportService.findReportById(reportId);

        return ResponseEntity.ok(Map.of(
                "code", "SUCCESS",
                "data", response
        ));
    }

    // 레포트 목록 조회
    @GetMapping("/reports")
    public ResponseEntity<?> getAllReports(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        ReportListResponse response = reportService.findAllReports(page, size);

        return ResponseEntity.ok(Map.of(
                "code", "SUCCESS",
                "data", response
        ));
    }

    // gpt 다시 요약
    @PatchMapping("/reports/{reportId}/resummarize")
    public ResponseEntity<Map<String, String>> reSummarize(@PathVariable Long reportId) {
        String updatedSummary = reportService.reSummarize(reportId);

        Map<String, String> response = new HashMap<>();
        response.put("summary", updatedSummary);

        return ResponseEntity.ok(response);
    }


    // 임시 테스트용
    @PostMapping("/reports")
    public ResponseEntity<?> createDummyReport(@RequestBody ReportCreateRequest request) {
        Report report = Report.builder()
                .carId(request.getCarId())
                .inspectionId(request.getInspectionId())
                .content(request.getContent())
                .createdAt(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                .workerId(request.getWorkerId())
                .type(request.getType())
                .status(Report.Status.CREATED) // 혹은 SAVED
                .build();

        Report savedReport = reportRepository.save(report);

        return ResponseEntity.ok(Map.of("reportId", savedReport.getReportId(), "code", "SUCCESS"));
    }

}
