package org.example.authenticationservice.service;

import org.example.authenticationservice.service.interfaces.EmailEventService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service for producing email events and sending them to a Kafka topic.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EmailEventProducerImpl implements EmailEventService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    /**
     * Sends an email event to a specified Kafka topic.
     *
     * @param topic the Kafka topic to send the event to
     * @param data the email event data
     */
    @Override
    public void sendEmailEvent(String topic, String data) {
        log.info("Sending email event to topic: {}", topic);
        kafkaTemplate.send(topic, data);
        log.info("Email event sent successfully");
    }
}
