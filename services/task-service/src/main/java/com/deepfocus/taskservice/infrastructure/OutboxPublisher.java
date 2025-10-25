package com.deepfocus.taskservice.infrastructure;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class OutboxPublisher {
    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OutboxPublisher(OutboxRepository outboxRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.outboxRepository = outboxRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelayString = "${outbox.poll-ms:1000}")
    @Transactional
    public void publishPending() {
        List<OutboxMessage> pending = outboxRepository.findAndLockPending();
        for (OutboxMessage m : pending) {
            try {
                kafkaTemplate.send(m.getTopic(), m.getKey(), m.getPayload()).get();
                m.setStatus("SENT");
                m.setSentAt(java.time.Instant.now());
                outboxRepository.save(m);
            } catch (Exception e) {
                m.setStatus("FAILED");
                outboxRepository.save(m);
            }
        }
    }
}