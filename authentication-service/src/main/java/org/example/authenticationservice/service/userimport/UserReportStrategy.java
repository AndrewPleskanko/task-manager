package org.example.authenticationservice.service.userimport;

import org.example.authenticationservice.entity.User;
import org.example.authenticationservice.enums.UserReportType;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public interface UserReportStrategy {

    byte[] generateReport(List<User> users);

    UserReportType getType();

    String getFileExtension();

    default String generateReportName() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ROOT);
        String dateString = dateFormat.format(new Date());

        return "Users_report_" + dateString + getFileExtension();
    }
}
