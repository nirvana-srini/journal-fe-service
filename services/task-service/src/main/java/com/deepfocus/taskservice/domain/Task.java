package com.deepfocus.taskservice.domain;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tasks")
public class Task {
    @Id
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ElementCollection
    @CollectionTable(name = "task_tags", joinColumns = @JoinColumn(name = "task_id"))
    @Column(name = "tag")
    private List<String> tags;

    @Column(name = "interval_plan_id")
    private UUID intervalPlanId;

    @Version
    private Long version;

    private Instant createdAt;
    private Instant updatedAt;

    public Task() {}

    public static Task create(String title, String description, List<String> tags, UUID intervalPlanId) {
        Task t = new Task();
        t.id = UUID.randomUUID();
        t.title = title;
        t.description = description;
        t.tags = tags;
        t.intervalPlanId = intervalPlanId;
        t.version = 1L;
        t.createdAt = Instant.now();
        t.updatedAt = Instant.now();
        return t;
    }

    public void update(String title, String description, List<String> tags) {
        if (title != null) this.title = title;
        if (description != null) this.description = description;
        if (tags != null) this.tags = tags;
        this.updatedAt = Instant.now();
    }

    // getters and setters
    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public List<String> getTags() { return tags; }
    public UUID getIntervalPlanId() { return intervalPlanId; }
    public Long getVersion() { return version; }
}