package com.deepfocus.taskservice.infrastructure;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;
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

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> payload;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> headers;

    private String status; // PENDING, SENT, FAILED
    private Instant createdAt;
    private Instant sentAt;

    public OutboxMessage() {}

    public OutboxMessage(
            UUID aggregateId,
            String aggregateType,
            String topic,
            String key,
            Map<String, Object> payload,
            Map<String, Object> headers
    ) {
        this.aggregateId = aggregateId;
        this.aggregateType = aggregateType;
        this.topic = topic;
        this.key = key;
        this.payload = payload;
        this.headers = headers;
        this.status = "PENDING";
        this.createdAt = Instant.now();
    }

    // getters/setters...

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public UUID getAggregateId() { return aggregateId; }
    public void setAggregateId(UUID aggregateId) { this.aggregateId = aggregateId; }

    public String getAggregateType() { return aggregateType; }
    public void setAggregateType(String aggregateType) { this.aggregateType = aggregateType; }

    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public Map<String, Object> getPayload() { return payload; }
    public void setPayload(Map<String, Object> payload) { this.payload = payload; }

    public Map<String, Object> getHeaders() { return headers; }
    public void setHeaders(Map<String, Object> headers) { this.headers = headers; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getSentAt() { return sentAt; }
    public void setSentAt(Instant sentAt) { this.sentAt = sentAt; }
}
