# DeepFocus MVP Design

## Vision Recap
DeepFocus is a cross-platform focus coach that combines interval-based work sessions with adaptive guidance, collaborative accountability, and wellness insights. The MVP delivers the core focus workflow while laying the foundation for future intelligent and social experiences.

## Target Platforms
- **Mobile:** React Native application for iOS and Android with shared codebase.
- **Web:** Responsive Next.js client for browsers and desktop (via PWA packaging).
- **Wearables:** Apple Watch companion (Phase 2) driven by the mobile app; scoped out of MVP build but design accommodates notification hooks.

## Personas & Core Jobs
| Persona | Primary Jobs | MVP Support |
| --- | --- | --- |
| Student | Plan study blocks, stay accountable, review productivity trends | Task list, timer plan vs. actual analytics, reminders |
| Freelancer | Balance client work, capture notes, manage breaks | Projects/tags, notes per task, CSV export |
| Team Member | Align with teammates, report status, meet goals | Shared workspace roadmap (Phase 2), personal metrics, sync across devices |

## MVP Feature Scope
### 1. Task Management
- CRUD tasks with optional notes, due dates, and tags.
- Quick-add from clipboard paste.
- Local-first storage with background sync.

### 2. Interval Timer
- Start/pause/skip Pomodoro-style intervals.
- Auto-start toggle, planned vs. actual tracking.
- Configurable durations for focus, short break, long break.
- Daily goal progress visualization.

### 3. Sync & Accounts
- Email/password + social login via Auth0.
- Secure session handling, refresh tokens.
- Cross-device sync for tasks, timers, and settings.

### 4. Reporting
- Daily/weekly dashboard with streaks, completed intervals, focus accuracy.
- Export CSV for selected date range.

### 5. Notifications & Soundscape
- Push notifications for session transitions and goal reminders (FCM + APNs via OneSignal).
- In-app ambient ticking with adjustable volume (local assets).

### Deferred for Post-MVP
- Collaborative focus rooms & team analytics.
- AI-generated focus playlists and adaptive interval suggestions.
- Advanced wellness integrations (HRV, mindfulness partners).
- Native desktop app; Apple Watch execution.

## System Architecture
```
[Clients]
  ├─ React Native App (iOS/Android)
  └─ Next.js Web App
        ↓ GraphQL over HTTPS (AppSync gateway)
[Backend Services]
  ├─ Auth Service (Auth0)
  ├─ Focus API (NestJS microservice, GraphQL + REST)
  │     ├─ PostgreSQL (RDS) for relational data
  │     ├─ Redis (ElastiCache) for session state & rate limiting
  │     └─ Temporal worker for reminders & interval orchestration
  └─ Analytics/Reporting Service (event consumers, Redshift serverless)
[Infrastructure]
  ├─ AWS API Gateway/AppSync, Lambda resolvers for simple queries
  ├─ S3 for asset storage & CSV exports
  ├─ CloudFront CDN for web assets
  └─ EventBridge bus for domain events
```

## Data Model (Key Entities)
- **User** (id, profile, preferences, authId)
- **Workspace** (id, ownerId, plan, createdAt)
- **Task** (id, workspaceId, title, note, dueDate, tags[], status)
- **IntervalPlan** (id, taskId, focusDuration, breakDurations, goalIntervals)
- **IntervalSession** (id, planId, type, scheduledStart, actualStart, actualEnd, status)
- **DeviceSettings** (id, userId, platform, timerConfig, notificationPrefs)
- **MetricEvent** (id, userId, timestamp, eventType, payload)

## Client Architecture
- **State Management:** Zustand for local timer state + React Query for server sync.
- **Offline Strategy:** SQLite via WatermelonDB on mobile; IndexedDB for web; conflict resolution favors last-write with audit trail for manual recovery.
- **Design System:** Shared component library in Storybook with theming tokens; dark/light themes.
- **Routing:** React Navigation (mobile), Next.js file-based routing (web).

## Backend Services
### Focus API (NestJS)
- Modules: Auth, Task, Timer, Reporting, Notification, Settings.
- GraphQL schema with subscriptions for live timer events; REST fallback for integrations.
- Temporal workflows handle timer lifecycle and reminders to ensure durability if clients disconnect.

### Analytics Pipeline
- EventBridge captures domain events → Kinesis Firehose → Redshift Serverless for aggregation.
- Daily aggregation job generates metrics for dashboards; cached in Redis.

## Infrastructure & DevOps
- **IaC:** Terraform modules for VPC, RDS, ElastiCache, AppSync, API Gateway, Cognito integration, S3.
- **CI/CD:** GitHub Actions running lint, unit, integration tests, and automated deployments via AWS CodeBuild/CodeDeploy.
- **Observability:** OpenTelemetry instrumentation → Datadog for tracing/metrics/logs. Alerts on sync failures, timer drift, error rate.
- **Feature Flags:** LaunchDarkly to gate experimental features (e.g., adaptive intervals).

## Security & Compliance
- JWT access tokens with 1h lifetime, refresh tokens stored securely.
- RBAC: owner vs. member roles (future-proofing teams).
- PII encryption at rest (RDS, S3) and in transit (TLS 1.2+).
- GDPR-ready data export/delete; audit logging for admin actions.

## MVP Delivery Plan
1. **Sprint 0 – Foundations (2 weeks)**
   - Finalize requirements, set up repos (frontend, backend, infra).
   - Implement CI/CD skeleton, linting, test scaffolds.
   - Terraform base infra (dev environment).
2. **Sprint 1 – Task & Timer Core (2 weeks)**
   - Task CRUD UI + API, local persistence.
   - Timer state machine client + backend orchestration.
3. **Sprint 2 – Accounts & Sync (2 weeks)**
   - Auth0 integration, secure GraphQL endpoints.
   - Background sync engine, conflict resolution.
4. **Sprint 3 – Reporting & Export (2 weeks)**
   - Metrics dashboard, CSV export pipeline.
   - Instrumentation + analytics ingestion.
5. **Sprint 4 – Notifications & Polish (2 weeks)**
   - Push notifications, sound settings, performance tuning.
   - Beta testing, bug fixes, release prep (App Store/TestFlight, Play Store).

## Open Risks & Mitigations
- **Timer accuracy under poor network:** Temporal workflows and client-side drift correction.
- **Offline conflict complexity:** Start with last-write wins, monitor incidents, evolve to CRDT if needed.
- **Multi-platform UI consistency:** Shared design tokens and Storybook visual regression tests.

## Next Steps Before Coding
- Confirm budget for AWS & SaaS tooling (Auth0, LaunchDarkly, Datadog).
- Gather stakeholder sign-off on deferred items.
- Create Jira/Linear workspace with backlog derived from sprints.
