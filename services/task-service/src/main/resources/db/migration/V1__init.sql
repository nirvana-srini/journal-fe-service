-- Flyway migration for task-service schema (v1)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS tasks (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  title TEXT NOT NULL,
  description TEXT,
  tags TEXT[],
  interval_plan_id UUID,
  version BIGINT NOT NULL DEFAULT 1,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

CREATE TABLE IF NOT EXISTS interval_plan (
  plan_id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  cycles INTEGER NOT NULL,
  work_duration_secs INTEGER NOT NULL,
  break_duration_secs INTEGER NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- Append-only change_history for audit/conflict resolution
CREATE TABLE IF NOT EXISTS change_history (
  id BIGSERIAL PRIMARY KEY,
  aggregate_id UUID,
  aggregate_type TEXT,
  op_type TEXT,
  op_data JSONB,
  op_metadata JSONB,
  occurred_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);

-- Transactional outbox table
CREATE TABLE IF NOT EXISTS outbox (
  id BIGSERIAL PRIMARY KEY,
  aggregate_id UUID,
  aggregate_type TEXT,
  topic TEXT,
  key TEXT,
  payload JSONB,
  headers JSONB,
  status TEXT DEFAULT 'PENDING',
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
  sent_at TIMESTAMP WITH TIME ZONE
);

CREATE INDEX IF NOT EXISTS idx_outbox_status ON outbox (status);
CREATE INDEX IF NOT EXISTS idx_tasks_interval_plan_id ON tasks (interval_plan_id);

-- Idempotency table
CREATE TABLE IF NOT EXISTS idempotency (
  idempotency_key TEXT PRIMARY KEY,
  aggregate_id UUID,
  method TEXT,
  response_payload JSONB,
  created_at TIMESTAMP WITH TIME ZONE DEFAULT now(),
  updated_at TIMESTAMP WITH TIME ZONE DEFAULT now()
);
