package com.kwizera.services.impl;

import com.kwizera.domain.dao.TaskDAO;
import com.kwizera.domain.entities.Task;
import com.kwizera.services.TaskServices;

import javax.sql.DataSource;
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
    public Task createTask(Task task) {
        return taskDAO.save(task);
    }

    @Override
    public Task updateTask(Task task) {
        return taskDAO.update(task);
    }

    @Override
    public Task findTask(int taskId) {
        return taskDAO.findById(taskId);
    }
}
