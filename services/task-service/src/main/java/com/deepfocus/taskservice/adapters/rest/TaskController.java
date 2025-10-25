package com.deepfocus.taskservice.adapters.rest;

import com.deepfocus.taskservice.application.TaskService;
import com.deepfocus.taskservice.domain.Task;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/tasks")
public class TaskController {
    private final TaskService taskService;
    public TaskController(TaskService taskService) { this.taskService = taskService; }

    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody CreateTaskRequest req, @RequestHeader(value="Idempotency-Key", required = false) String idempotencyKey) {
        Task t = taskService.createTask(req.title, req.description, req.tags, req.intervalPlanId);
        return ResponseEntity.status(201).body(t);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTask(@PathVariable UUID taskId) {
        return ResponseEntity.ok(taskService.getTask(taskId));
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable UUID taskId, @Valid @RequestBody UpdateTaskRequest req) {
        Task t = taskService.updateTask(taskId, req.title, req.description, req.tags, req.version);
        return ResponseEntity.ok(t);
    }

    public static class CreateTaskRequest {
        public String title;
        public String description;
        public List<String> tags;
        public UUID intervalPlanId;
    }

    public static class UpdateTaskRequest {
        public String title;
        public String description;
        public List<String> tags;
        public Long version;
    }
}