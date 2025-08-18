package aivle.project.report.event;

import aivle.project.report.dto.request.WorkerTaskCompletedEventDTO;
import aivle.project.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WorkerTaskCompletedEventConsumer {

    private final ReportService reportService;

    @KafkaListener(
            topics = "worker-task-completed",
            groupId = "report-group"
    )
    public void consume(WorkerTaskCompletedEventDTO event) {
        log.info("[kafka] worker-task-completed 이벤트 수신: {}", event);

        try {
            reportService.saveReportWithGpt(event);
            log.info("[kafka] report 저장 완료: auditId={}, workerName={}",
                    event.getAuditId(), event.getWorkerName());
        } catch (Exception e) {
            log.error("[kafka] report 저장 중 오류 발생: {}", e.getMessage(), e);
        }
    }
}
