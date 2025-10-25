# DeepFocus — Low-Level Design (MVP)

Purpose
- Focused low-level design for implementing core DeepFocus functionality for the MVP: Task management, Interval timer (plans + sessions), sync, and telemetry. Target stack: Java Spring Boot microservices, Keycloak (OIDC), Kafka, Temporal, PostgreSQL, MinIO, k8s, OpenTelemetry, Prometheus/Grafana/Loki/Tempo.

Scope
- This document covers service boundaries, data ownership, key DB schemas, REST API contracts (refer to api/openapi/*.yaml), Kafka event shapes (events/schemas/*.json), Temporal workflow responsibilities, conflict and idempotency rules, and minimal observability requirements.

Service boundaries (owner -> responsibilities)
- task-service: tasks, tags, notes, task-level interval plans. Owns tasks schema, interval_plan metadata.
- timer-service: session lifecycle, Temporal workflows, durable timers, emits session events. Owns interval_session records.
- sync-gateway: handles device sync ops, reconciliation, optimistic locking and conflict reporting.
- user-service: user profiles, preferences, device settings (including timer_config and notification_prefs).
- notification-service: push adapters for APNs/FCM and scheduling interface used by Temporal.
- reporting-service: consumes events to produce aggregates and CSV exports (writes to MinIO).

Data ownership and storage
- Each service owns its Postgres schema (single DB instance per service recommended for ops isolation).
- Use Flyway for schema migrations. Use UUID PKs and bigserial/sequence for monotonic ordering only when needed.
- Change history: keep an append-only change_history table per service to aid conflict resolution and audit.

Key tables (see db/sql/task-service-schema.sql for DDL)
- tasks (task-service)
- interval_plan (task-service)
- interval_session (timer-service)
- change_history (task-service)
- device_settings (user-service)

REST API design (see api/openapi/*.yaml for machine-readable contracts)
- REST is used for service-to-service and client-to-service communication. OpenAPI v3 specs included for task-service and timer-service.
- Security: Bearer OIDC tokens (Keycloak). Each spec contains an OIDC security scheme reference.

Idempotency and conflict handling
- Client ops include an opId/idempotency-key header for operations that must be idempotent (e.g., session start, task create during sync).
- Optimistic locking: responses include a version field (int64) and updates must include version. Server returns 409 and current entity when version mismatch occurs.
- Sync-gateway accepts ordered lists of ops from devices and returns applied ops and conflicts. For MVP, conflict resolution policy: last-write-wins for non-critical fields and explicit conflict returned for fields marked "user-resolvable" (title, note).

Events and Topic naming (Kafka)
- topic: task.events (key: taskId) — events: task.created, task.updated, task.deleted
- topic: session.events (key: sessionId) — events: session.started, session.paused, session.completed, session.cancelled
- topic: metric.events — lightweight telemetry events
- event envelope format: see events/schemas/*.json

Temporal integration (timer-service)
- timer-service starts a Temporal workflow when a session is started; workflow persists state and emits session.completed on natural expiry.
- Workflows use durable timers, heartbeats, and can call notification-service to push state transitions.
- The interval_session row stores temporal_workflow_id to map DB to workflow.

Observability (MVP minimum)
- Use OpenTelemetry Java auto-instrumentation (otel-javaagent) + Micrometer Prometheus.
- Expose /actuator/prometheus and /actuator/health.
- Ensure logs are structured JSON and include trace_id and span_id for correlation.
- Add business metrics: tasks.created, tasks.updated, sessions.started, sessions.completed, sync.conflicts.

Operational notes
- Use Helm charts per service. Use ArgoCD for GitOps deployment.
- Namespaces: dev, staging, prod. Use immutable image tags (git-sha). Use resource requests/limits and HPA.
- Backup/DR: schedule regular pg backups (pgBackRest), MinIO bucket replication, and test restores.

Next artifacts
- OpenAPI specs: api/openapi/task-service.yaml and api/openapi/timer-service.yaml
- Event schemas: events/schemas/task.events.json and events/schemas/session.events.json
- SQL DDL: db/sql/task-service-schema.sql