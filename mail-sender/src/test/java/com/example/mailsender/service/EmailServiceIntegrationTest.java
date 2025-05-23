package com.example.mailsender.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;

import com.example.mailsender.MailSenderApplication;
import com.example.mailsender.entity.EmailMessage;
import com.example.mailsender.enums.MessageSendingStatus;
import com.example.mailsender.repository.EmailMessageRepository;

@SpringBootTest
@ContextConfiguration(classes = {MailSenderApplication.class, BaseServiceTest.class})
class EmailServiceIntegrationTest {

    @Autowired
    private EmailMessageRepository emailMessageRepository;

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    @Autowired
    private EmailServiceImpl emailService;

    @BeforeEach
    public void beforeEach() {
        IndexOperations indexOps = elasticsearchOperations.indexOps(EmailMessage.class);
        if (indexOps.exists()) {
            indexOps.delete();
        }
        indexOps.create();
        indexOps.createMapping();
    }

    @AfterEach
    public void afterEach() {
        elasticsearchOperations.indexOps(EmailMessage.class).delete();
    }

    @MockBean
    private JavaMailSender mailSender;

    @Test
    public void sendEmail_withValidEmail_sendsEmailSuccessfully() {
        // given
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setRecipientEmail("test@example.com");
        emailMessage.setSubject("Test Subject");
        emailMessage.setContent("Test Content");

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        // when
        emailService.sendEmail(emailMessage);

        // then
        verify(mailSender).send(any(SimpleMailMessage.class));
        List<EmailMessage> savedMessages = emailMessageRepository.findByRecipientEmail("test@example.com");
        assertEquals(MessageSendingStatus.SENT, savedMessages.get(0).getStatus());
        Assertions.assertFalse(savedMessages.isEmpty(), "No EmailMessage found with the given recipient email");
    }

    @Test
    public void sendEmail_withInvalidEmail_setsStatusToError() {
        // given
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setRecipientEmail("invalid_email");
        emailMessage.setSubject("Test Subject");
        emailMessage.setContent("Test Content");

        doThrow(new MailSendException("error")).when(mailSender).send(any(SimpleMailMessage.class));

        // when
        emailService.sendEmail(emailMessage);

        // then
        verify(mailSender).send(any(SimpleMailMessage.class));
        assertEquals(MessageSendingStatus.ERROR, emailMessage.getStatus());
    }

    @Test
    void resendFailedEmails_withMaxAttemptsReached_doesNotResendEmails() {
        // given
        EmailMessage failedEmail = new EmailMessage();
        failedEmail.setId("100");
        failedEmail.setRecipientEmail("test@example.com");
        failedEmail.setSubject("Test Subject");
        failedEmail.setContent("Test Content");
        failedEmail.setStatus(MessageSendingStatus.ERROR);
        failedEmail.setAttemptCount(4);

        // when
        emailService.resendFailedEmails();

        // then
        Optional<EmailMessage> optionalEmailMessage = emailMessageRepository.findById(failedEmail.getId());
        assertTrue(optionalEmailMessage.isEmpty());
    }

    @Test
    void resendFailedEmails_withFailedEmails_setsStatusToSent() {
        // given
        EmailMessage failedEmail = new EmailMessage();
        failedEmail.setId("100");
        failedEmail.setRecipientEmail("test@example.com");
        failedEmail.setSubject("Test Subject");
        failedEmail.setContent("Test Content");
        failedEmail.setStatus(MessageSendingStatus.ERROR);
        failedEmail.setAttemptCount(2);
        emailMessageRepository.save(failedEmail);

        // when
        doNothing().when(mailSender).send(any(SimpleMailMessage.class));
        emailService.resendFailedEmails();

        // then
        Optional<EmailMessage> optionalEmailMessage = emailMessageRepository.findById(failedEmail.getId());
        assertTrue(optionalEmailMessage.isPresent());
        assertEquals(MessageSendingStatus.SENT, optionalEmailMessage.get().getStatus());
    }
}