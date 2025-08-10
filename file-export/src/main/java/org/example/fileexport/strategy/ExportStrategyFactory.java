package org.example.fileexport.strategy;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

@Component
public class ExportStrategyFactory {
    private final Map<String, ExportStrategy> strategies;

    public ExportStrategyFactory(List<ExportStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(
                        s -> s.getType().toLowerCase(),
                        Function.identity()
                ));
    }

    public ExportStrategy getStrategy(String format) {
        ExportStrategy strategy = strategies.get(format.toLowerCase());
        if (strategy == null) {
            throw new IllegalArgumentException("No export strategy found for format: " + format);
        }
        return strategy;
    }
}


