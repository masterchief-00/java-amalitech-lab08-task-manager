package com.kwizera.domain.entities;

import java.time.LocalDate;
import java.util.Optional;

public class Project {
    private int id;
    private String title;
    private String description;
    private int employeeId;
    private LocalDate due;
    private LocalDate createdAt;
    private Optional<LocalDate> updatedAt;

    public Project(int id, String title, String description, int employeeId, LocalDate due) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.employeeId = employeeId;
        this.due = due;
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

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDate getDue() {
        return due;
    }

    public void setDue(LocalDate due) {
        this.due = due;
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
}
