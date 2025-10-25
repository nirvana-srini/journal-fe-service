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

    public IdempotencyEntry() {}

    // getters/setters omitted
}