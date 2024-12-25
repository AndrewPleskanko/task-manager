package com.example.mailsender.repository;

import com.example.mailsender.entity.EmailMessage;
import com.example.mailsender.enums.MessageSendingStatus;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface EmailMessageRepository extends ElasticsearchRepository<EmailMessage, String> {
    List<EmailMessage> findByStatus(MessageSendingStatus status);

    List<EmailMessage> findByRecipientEmail(String recipientEmail);
}