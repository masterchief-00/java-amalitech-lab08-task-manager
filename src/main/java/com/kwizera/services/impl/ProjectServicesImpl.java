package com.kwizera.services.impl;

import com.kwizera.domain.dao.ProjectDAO;
import com.kwizera.domain.entities.Project;
import com.kwizera.services.ProjectServices;

import java.util.List;

public class ProjectServicesImpl implements ProjectServices {
    private final ProjectDAO projectDAO;

    public ProjectServicesImpl(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    @Override
    public Project getProject(int projectId) {
        return projectDAO.findById(projectId);
    }

    @Override
    public List<Project> getProjects(int userId, int limit, int page) {
        return projectDAO.findAll(userId, limit, page);
    }

    @Override
    public void createProject(Project project) {
        projectDAO.save(project);
    }

    @Override
    public void delete(int projectId) {
        projectDAO.delete(projectId);
    }
}
