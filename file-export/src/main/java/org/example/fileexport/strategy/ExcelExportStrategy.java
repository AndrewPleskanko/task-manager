package org.example.fileexport.strategy;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.function.Function;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.fileexport.enums.ReportType;
import org.example.fileexport.exception.ReportGenerationException;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Strategy for generating reports in Excel format.
 */
@Slf4j
@Component
public class ExcelExportStrategy implements ExportStrategy {

    @Override
    public <T> byte[] generateReport(List<T> items, List<String> headers, Function<T, List<Object>> rowMapper) {
        log.info("Generating Excel report for {} items", items.size());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Report");

            // Header
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.size(); i++) {
                headerRow.createCell(i).setCellValue(headers.get(i));
            }

            // Data
            for (int i = 0; i < items.size(); i++) {
                List<Object> rowData = rowMapper.apply(items.get(i));
                Row row = sheet.createRow(i + 1);
                for (int j = 0; j < rowData.size(); j++) {
                    Object value = rowData.get(j);
                    if (value instanceof Number) {
                        row.createCell(j).setCellValue(((Number) value).doubleValue());
                    } else {
                        row.createCell(j).setCellValue(value != null ? value.toString() : "");
                    }
                }
            }

            for (int i = 0; i < headers.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
        } catch (Exception e) {
            log.error("Error while generating EXCEL report", e);
            throw new ReportGenerationException(ReportType.EXCEL.name());
        }

        return outputStream.toByteArray();
    }

    @Override
    public String getType() {
        return ReportType.EXCEL.name();
    }

    @Override
    public String getFileExtension() {
        return ".xlsx";
    }
}
