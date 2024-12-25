package com.example.mailsender.service.inrerfaces;

import com.example.mailsender.entity.EmailMessage;

public interface EmailService {
    void sendEmail(EmailMessage emailMessage);

    void resendFailedEmails();
}

