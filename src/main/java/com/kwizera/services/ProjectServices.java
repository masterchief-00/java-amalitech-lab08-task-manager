package com.kwizera.services;

import com.kwizera.domain.entities.Project;

import java.util.List;

public interface ProjectServices {
    Project getProject(int projectId);

    List<Project> getProjects(int userId, int limit, int page);

    void createProject(Project project);

    void delete(int projectId);
}
