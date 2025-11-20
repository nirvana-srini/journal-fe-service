package com.deepfocus.taskservice.infrastructure;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "idempotency")
public class IdempotencyEntry {
    @Id
    private String idempotencyKey;

    private UUID aggregateId;
    private String method;
    @Column(columnDefinition = "jsonb")
    private String responsePayload;
    private Instant createdAt;
    private Instant updatedAt;

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(UUID aggregateId) {
        this.aggregateId = aggregateId;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getResponsePayload() {
        return responsePayload;
    }

    public void setResponsePayload(String responsePayload) {
        this.responsePayload = responsePayload;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public IdempotencyEntry() {}

}