-- DDL for task-service (Postgres)

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS tasks (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  workspace_id UUID NOT NULL,
  user_id UUID NOT NULL,
  title TEXT NOT NULL,
  note TEXT,
  tags JSONB DEFAULT '[]'::jsonb,
  due_date timestamptz,
  status TEXT NOT NULL DEFAULT 'todo',
  version BIGINT NOT NULL DEFAULT 1,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_tasks_workspace ON tasks (workspace_id);
CREATE INDEX IF NOT EXISTS idx_tasks_user ON tasks (user_id);
CREATE INDEX IF NOT EXISTS idx_tasks_status ON tasks (status);
CREATE INDEX IF NOT EXISTS idx_tasks_due_date ON tasks (due_date);
CREATE INDEX IF NOT EXISTS idx_tasks_tags ON tasks USING GIN (tags);

CREATE TABLE IF NOT EXISTS interval_plan (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  task_id UUID NOT NULL REFERENCES tasks(id) ON DELETE CASCADE,
  focus_duration INT NOT NULL,
  short_break_duration INT NOT NULL,
  long_break_duration INT NOT NULL,
  goal_intervals INT NOT NULL DEFAULT 1,
  created_by UUID NOT NULL,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now()
);

CREATE TABLE IF NOT EXISTS change_history (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  entity_type TEXT NOT NULL,
  entity_id UUID NOT NULL,
  op_id TEXT NOT NULL,
  user_id UUID,
  payload JSONB,
  created_at timestamptz NOT NULL DEFAULT now()
);

-- interval_session is owned by timer-service; simple schema example here for reference
CREATE TABLE IF NOT EXISTS interval_session (
  id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
  plan_id UUID NOT NULL,
  type TEXT NOT NULL,
  scheduled_start timestamptz,
  actual_start timestamptz,
  actual_end timestamptz,
  status TEXT NOT NULL DEFAULT 'scheduled',
  temporal_workflow_id TEXT,
  started_by_device_id TEXT,
  metadata JSONB,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now()
);
