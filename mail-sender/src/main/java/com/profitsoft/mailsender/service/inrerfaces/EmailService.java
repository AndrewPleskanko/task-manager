package com.profitsoft.mailsender.service.inrerfaces;

import com.profitsoft.mailsender.entity.EmailMessage;

public interface EmailService {
    void sendEmail(EmailMessage emailMessage);

    void resendFailedEmails();
}

