package org.example.fileexport.strategy;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.function.Function;

import org.example.fileexport.enums.ReportType;
import org.example.fileexport.exception.ReportGenerationException;
import org.springframework.stereotype.Component;

import com.opencsv.CSVWriter;
import lombok.extern.slf4j.Slf4j;

/**
 * Strategy for generating reports in CSV format.
 */
@Slf4j
@Component
public class CsvExportStrategy implements ExportStrategy {

    @Override
    public <T> byte[] generateReport(List<T> items, List<String> headers, Function<T, List<Object>> rowMapper) {
        log.info("Starting CSV report generation");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (Writer writer = new OutputStreamWriter(byteArrayOutputStream);
             CSVWriter csvWriter = new CSVWriter(writer)) {

            if (headers != null && !headers.isEmpty()) {
                csvWriter.writeNext(headers.toArray(new String[0]));
            }

            for (T item : items) {
                List<Object> rowData = rowMapper.apply(item);
                String[] row = rowData.stream()
                        .map(obj -> obj != null ? obj.toString() : "")
                        .toArray(String[]::new);
                csvWriter.writeNext(row);
            }

        } catch (Exception e) {
            log.error("Error while generating CSV report", e);
            throw new ReportGenerationException(ReportType.CSV.name(), e);
        }

        log.debug("CSV report generated successfully");
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public String getType() {
        return ReportType.CSV.name();
    }

    @Override
    public String getFileExtension() {
        return ".csv";
    }

}
