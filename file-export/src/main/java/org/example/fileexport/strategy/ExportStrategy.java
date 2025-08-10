package org.example.fileexport.strategy;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.function.Function;

public interface ExportStrategy {
    <T> byte[] generateReport(List<T> items, List<String> headers, Function<T, List<Object>> rowMapper);

    String getType();

    String getFileExtension();

    default String generateReportName(String prefix) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ROOT);
        String dateString = dateFormat.format(new Date());
        return prefix + "_report_" + dateString + getFileExtension();
    }
}
