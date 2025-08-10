package org.example.fileexport.exception;

import java.io.Serial;

import org.example.fileexport.enums.ReportType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class UnsupportedReportTypeException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    public UnsupportedReportTypeException(ReportType reportType) {
        super("Unsupported user report type: " + reportType);
    }
}
