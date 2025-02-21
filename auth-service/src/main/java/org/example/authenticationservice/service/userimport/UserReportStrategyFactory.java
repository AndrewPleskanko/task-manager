package org.example.authenticationservice.service.userimport;

import java.util.List;

import org.example.authenticationservice.enums.UserReportType;
import org.example.authenticationservice.exception.UnsupportedUserReportTypeException;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserReportStrategyFactory {

    private final List<UserReportStrategy> strategies;

    public UserReportStrategy lookup(UserReportType reportType) {
        return strategies.stream()
                .filter(strategy -> reportType.equals(strategy.getType()))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Unsupported user report type: {}", reportType);
                    throw new UnsupportedUserReportTypeException(reportType);
                });
    }
}
