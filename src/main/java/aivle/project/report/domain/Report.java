package aivle.project.report.domain;

import aivle.project.report.domain.enumerate.InspectionType;
import aivle.project.report.dto.request.WorkerTaskCompletedEventDTO;
import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Entity
@Table(name = "reports")
@Getter  @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    private Long auditId;

    private Long inspectionId;
    private Long workerId;
    private String workerName;

    @Enumerated(EnumType.STRING)
    private InspectionType type;

    // 작업자 원본 입력 값
    @Column(columnDefinition = "TEXT")
    private String rawContent;

    // 정돈된 문장
    @Column(columnDefinition = "TEXT")
    private String resolve;

    // 요약
    @Column(columnDefinition = "TEXT")
    private String summary;

    private LocalDateTime createdAt;  // 레포트 생성 일자
    private LocalDateTime startedAt;  // 조치 시작 시간
    private LocalDateTime endedAt;    // 조치 완료 시간

    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus;   // 리포트의 처리 상태 (CREATED, SAVED 등)

    public enum ReportStatus {
        CREATED, SAVED
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    public static Report toEntity(WorkerTaskCompletedEventDTO dto, String refinedContent, String summary, InspectionType type) {
        return Report.builder()
                .auditId(dto.getAuditId())
                .inspectionId(dto.getInspectionId())
                .workerId(dto.getWorkerId())
                .workerName(dto.getWorkerName())
                .rawContent(dto.getResolve())
                .resolve(refinedContent)
                .summary(summary)
                .type(type)
                .startedAt(dto.getStartedAt())
                .endedAt(dto.getEndedAt())
                .build();
    }
}

