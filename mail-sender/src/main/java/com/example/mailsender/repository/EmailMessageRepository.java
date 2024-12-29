package com.example.mailsender.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.example.mailsender.entity.EmailMessage;
import com.example.mailsender.enums.MessageSendingStatus;

public interface EmailMessageRepository extends ElasticsearchRepository<EmailMessage, String> {
    List<EmailMessage> findByStatus(MessageSendingStatus status);

    List<EmailMessage> findByRecipientEmail(String recipientEmail);
}