package com.kwizera.services;

import com.kwizera.domain.entities.Task;
import com.kwizera.domain.entities.TaskPriority;
import com.kwizera.domain.entities.TaskStatus;

import java.util.List;

public interface TaskServices {
    List<Task> getAllTasks(int projectId, int limit, int page);

    List<Task> getTasksByStatus(int projectId, TaskStatus status);

    List<Task> getTasksByPriority(int projectId, TaskPriority priority);

    Task createTask(Task task);

    void updateTask(Task task);

    Task findTask(int taskId);
}
