package aivle.project.report.repository;

import aivle.project.report.domain.Report;
import aivle.project.report.dto.response.ReportListResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    List<Report> findByWorkerId(Long workerId);

}

