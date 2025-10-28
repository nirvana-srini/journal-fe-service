-- Create the element-collection table for Task.tags

CREATE TABLE IF NOT EXISTS task_tags (
    task_id UUID NOT NULL,
    tag TEXT NOT NULL,
    -- You usually don't strictly need a surrogate PK for @ElementCollection,
    -- but it's nice for tooling, so we'll add one.
    id BIGSERIAL PRIMARY KEY,

    CONSTRAINT fk_task_tags_task
      FOREIGN KEY (task_id)
      REFERENCES tasks (id)
      ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS idx_task_tags_task_id ON task_tags (task_id);
CREATE INDEX IF NOT EXISTS idx_task_tags_tag ON task_tags (tag);
