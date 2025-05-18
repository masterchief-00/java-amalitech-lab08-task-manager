package com.kwizera.domain.dao;

import com.kwizera.domain.entities.Task;
import com.kwizera.domain.entities.TaskPriority;
import com.kwizera.domain.entities.TaskStatus;

import java.util.List;

public interface TaskDAO {
    Task findById(int taskId);

    List<Task> findAll(int projectId, int limit, int page);

    List<Task> priorityFiltered(int projectId, TaskPriority priority);

    List<Task> statusFiltered(int projectId, TaskStatus status);

    List<Task> overdueFiltered(int projectId);

    Task save(Task task);

    Task update(Task task);

    void delete(int taskId);
}
