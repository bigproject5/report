package aivle.project.report.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 테스트용 코드
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReportCreateRequest {
    private String carId;
    private Long inspectionId;
    private String content;
    private Long workerId;
}

