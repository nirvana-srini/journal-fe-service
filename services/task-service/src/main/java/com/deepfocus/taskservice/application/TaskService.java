package com.deepfocus.taskservice.application;

import com.deepfocus.taskservice.domain.Task;
import com.deepfocus.taskservice.infrastructure.OutboxMessage;
import com.deepfocus.taskservice.infrastructure.OutboxRepository;
import com.deepfocus.taskservice.infrastructure.TaskRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final OutboxRepository outboxRepository;

    public TaskService(TaskRepository taskRepository, OutboxRepository outboxRepository) {
        this.taskRepository = taskRepository;
        this.outboxRepository = outboxRepository;
    }

    @Transactional
    public Task createTask(String title, String description, List<String> tags, UUID intervalPlanId) {
        Task t = Task.create(title, description, tags, intervalPlanId);
        taskRepository.save(t);

        String payload = String.format("{\\"event_type\\":\\"task.created\\",\\"task\\": {\\"id\\":\\"%s\\",\\"title\\":\\"%s\\"}}", t.getId(), t.getTitle());
        OutboxMessage o = new OutboxMessage(t.getId(), "task", "task.events", t.getId().toString(), payload, "{}");
        outboxRepository.save(o);

        return t;
    }

    @Transactional
    public Task updateTask(UUID id, String title, String description, List<String> tags, Long expectedVersion) {
        Task t = taskRepository.findById(id).orElseThrow();
        if (expectedVersion != null && !t.getVersion().equals(expectedVersion)) {
            throw new RuntimeException("Version conflict");
        }
        t.update(title, description, tags);
        taskRepository.save(t);

        String payload = String.format("{\\"event_type\\":\\"task.updated\\",\\"task\\": {\\"id\\":\\"%s\\",\\"title\\":\\"%s\\"}}", t.getId(), t.getTitle());
        OutboxMessage o = new OutboxMessage(t.getId(), "task", "task.events", t.getId().toString(), payload, "{}");
        outboxRepository.save(o);

        return t;
    }

    public Task getTask(UUID id) {
        return taskRepository.findById(id).orElseThrow();
    }
}