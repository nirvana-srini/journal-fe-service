package com.deepfocus.taskservice.infrastructure;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox")
public class OutboxMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID aggregateId;
    private String aggregateType;
    private String topic;
    private String key;

    @Column(columnDefinition = "jsonb")
    private String payload;

    @Column(columnDefinition = "jsonb")
    private String headers;

    private String status; // PENDING, SENT, FAILED

    private Instant createdAt;
    private Instant sentAt;

    public OutboxMessage() {}
    public OutboxMessage(java.util.UUID aggregateId, String aggregateType, String topic, String key, String payload, String headers){
        this.aggregateId = aggregateId;
        this.aggregateType = aggregateType;
        this.topic = topic;
        this.key = key;
        this.payload = payload;
        this.headers = headers;
        this.status = "PENDING";
        this.createdAt = Instant.now();
    }

    // getters/setters omitted for brevity
}