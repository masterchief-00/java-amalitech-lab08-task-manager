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
    employee_id INTEGER      NOT NULL NULL REFERENCES employee (id),
    created_at  TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP
);

-- ENUMS
CREATE TYPE priority_enum AS ENUM ('HIGH','MEDIUM','LOW');
CREATE TYPE status_enum AS ENUM ('PENDING','COMPLETED','CANCELLED');

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
CREATE INDEX idx_employee_email ON employee (email);
CREATE INDEX idx_projects_employee_id ON project (employee_id);
CREATE INDEX idx_task_project_id_status_priority ON task (project_id, status, priority);
CREATE INDEX idx_task_project_id_due ON task (project_id, due);