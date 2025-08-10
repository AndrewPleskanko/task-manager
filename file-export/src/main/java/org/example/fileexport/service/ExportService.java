package org.example.fileexport.service;

import java.util.List;
import java.util.function.Function;

import org.example.fileexport.strategy.ExportStrategy;
import org.example.fileexport.strategy.ExportStrategyFactory;
import org.springframework.stereotype.Service;

@Service
public class ExportService {

    private final ExportStrategyFactory exportStrategyFactory;

    public ExportService(ExportStrategyFactory exportStrategyFactory) {
        this.exportStrategyFactory = exportStrategyFactory;
    }

    public <T> byte[] exportReport(String format, List<T> items, List<String> headers,
                                   Function<T, List<Object>> rowMapper) {
        ExportStrategy strategy = exportStrategyFactory.getStrategy(format);
        if (strategy == null) {
            throw new IllegalArgumentException("No export strategy found for format: " + format);
        }
        return strategy.generateReport(items, headers, rowMapper);
    }
}
