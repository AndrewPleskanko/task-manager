package com.example.mailsender.entity;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import com.example.mailsender.enums.MessageSendingStatus;
import lombok.Data;

@Data
@Document(indexName = "email_message")
public class EmailMessage {

    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String subject;

    @Field(type = FieldType.Text)
    private String content;

    @Field(type = FieldType.Keyword)
    private String recipientEmail;

    @Field(type = FieldType.Keyword)
    private MessageSendingStatus status;

    @Field(type = FieldType.Date)
    private LocalDate lastAttemptTime;

    @Field(type = FieldType.Integer)
    private int attemptCount;
}