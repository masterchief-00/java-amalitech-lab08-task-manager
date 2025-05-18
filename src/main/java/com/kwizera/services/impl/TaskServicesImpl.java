package com.kwizera.services.impl;

import com.kwizera.domain.dao.TaskDAO;
import com.kwizera.domain.entities.Task;
import com.kwizera.domain.entities.TaskPriority;
import com.kwizera.domain.entities.TaskStatus;
import com.kwizera.services.TaskServices;

import java.util.List;

public class TaskServicesImpl implements TaskServices {
    private final TaskDAO taskDAO;

    public TaskServicesImpl(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    @Override
    public List<Task> getAllTasks(int projectId, int limit, int page) {
        return taskDAO.findAll(projectId, limit, page);
    }

    @Override
    public List<Task> getTasksByStatus(int projectId, TaskStatus status) {
        return taskDAO.statusFiltered(projectId, status);
    }

    @Override
    public List<Task> getTasksByPriority(int projectId, TaskPriority priority) {
        return taskDAO.priorityFiltered(projectId, priority);
    }

    @Override
    public Task createTask(Task task) {
        return taskDAO.save(task);
    }

    @Override
    public void updateTask(Task task) {
        taskDAO.update(task);
    }

    @Override
    public Task findTask(int taskId) {
        return taskDAO.findById(taskId);
    }
}
