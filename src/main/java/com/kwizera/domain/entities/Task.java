package com.kwizera.domain.entities;

import java.time.LocalDate;
import java.util.Optional;

public class Task {
    private int id;
    private String title;
    private String description;
    private TaskPriority priority;
    private LocalDate due;
    private TaskStatus status;
    private Project project;
    private LocalDate createdAt;
    private Optional<LocalDate> updatedAt;

    public Task(int id, String title, String description, TaskPriority priority, LocalDate due, TaskStatus status, Project project) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.due = due;
        this.status = status;
        this.project = project;
        this.createdAt = LocalDate.now();
        this.updatedAt = Optional.empty();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TaskPriority getPriority() {
        return priority;
    }

    public void setPriority(TaskPriority priority) {
        this.priority = priority;
    }

    public LocalDate getDue() {
        return due;
    }

    public void setDue(LocalDate due) {
        this.due = due;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public Optional<LocalDate> getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = Optional.ofNullable(updatedAt);
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
