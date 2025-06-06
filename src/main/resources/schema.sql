-- ENUMS
DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'priority_enum') THEN
            CREATE TYPE priority_enum AS ENUM ('HIGH','MEDIUM','LOW');
        END IF;
    END
$$;

DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'status_enum') THEN
            CREATE TYPE status_enum AS ENUM ('PENDING','COMPLETED','CANCELLED');
        END IF;
    END
$$;

-- EMPLOYEE TABLE
CREATE TABLE IF NOT EXISTS employee
(
    id         SERIAL PRIMARY KEY,
    first_name VARCHAR(100)        NOT NULL,
    last_name  VARCHAR(100)        NOT NULL,
    email      VARCHAR(100) UNIQUE NOT NULL,
    password   TEXT                NOT NULL
);

-- PROJECT TABLE
CREATE TABLE IF NOT EXISTS project
(
    id          SERIAL PRIMARY KEY,
    title       VARCHAR(250) NOT NULL,
    description TEXT         NOT NULL,
    due         DATE         NOT NULL,
    employee_id INTEGER      NOT NULL REFERENCES employee (id),
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP
);

-- TASK TABLE
CREATE TABLE IF NOT EXISTS task
(
    id          SERIAL PRIMARY KEY,
    title       VARCHAR(100)  NOT NULL,
    description TEXT          NOT NULL,
    priority    priority_enum NOT NULL,
    due         DATE          NOT NULL,
    status      status_enum              DEFAULT 'PENDING',
    project_id  INTEGER       NOT NULL REFERENCES project (id),
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP
);

-- INDEXES
CREATE INDEX IF NOT EXISTS idx_employee_email ON employee (email);
CREATE INDEX IF NOT EXISTS idx_employee_id ON employee (id);
CREATE INDEX IF NOT EXISTS idx_projects_employee_id_due ON project (employee_id, due);
CREATE INDEX IF NOT EXISTS idx_projects_id ON project (id);
CREATE INDEX IF NOT EXISTS idx_task_project_id_status_priority ON task (project_id, status, priority);
CREATE INDEX IF NOT EXISTS idx_task_project_id_due ON task (project_id, due);