package com.kwizera.services;

import com.kwizera.domain.entities.Task;

import java.util.List;

public interface TaskServices {
    List<Task> getAllTasks(int projectId, int limit, int page);

    Task createTask(Task task);

    Task updateTask(Task task);

    Task findTask(int taskId);
}
