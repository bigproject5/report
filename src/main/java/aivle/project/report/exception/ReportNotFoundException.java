package aivle.project.report.exception;

public class ReportNotFoundException extends RuntimeException {
    public ReportNotFoundException() {
        super("해당 레포트가 존재하지 않습니다.");
    }
}

