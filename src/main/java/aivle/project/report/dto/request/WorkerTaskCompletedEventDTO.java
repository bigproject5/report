package aivle.project.report.dto.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WorkerTaskCompletedEventDTO {
    private Long auditId;
    private String workerName;
    private Long inspectionId;
    private String resolve;
    private Long workerId;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    private String type;
}

