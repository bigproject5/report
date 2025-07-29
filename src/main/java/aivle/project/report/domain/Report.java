package aivle.project.report.domain;

import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Id;

import java.time.LocalDate;

@Entity
@Table(name = "reports")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    private String carId;

    private Long inspectionId;

    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDate createAt;

    private Long workerId;

    @Enumerated(EnumType.STRING)
    private Status status;

    public enum Status {
        CREATED, SAVED
    }
}

