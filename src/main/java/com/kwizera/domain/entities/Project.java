package com.kwizera.domain.entities;

import java.time.LocalDate;
import java.util.Optional;

public class Project {
    private int id;
    private String title;
    private String description;
    private Employee employee;
    private LocalDate due;
    private LocalDate createdAt;
    private Optional<LocalDate> updatedAt;

    public Project(int id, String title, String description, Employee employee, LocalDate due) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.employee = employee;
        this.due = due;
        this.createdAt = LocalDate.now();
        this.updatedAt = Optional.empty();
    }

    public Project(int id, String title, String description, Employee employee, LocalDate due, LocalDate createdAt, Optional<LocalDate> updatedAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.employee = employee;
        this.due = due;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
