package com.kwizera.domain.dao;

import com.kwizera.domain.entities.Project;

import java.util.List;
import java.util.Optional;

public interface ProjectDAO {

    void save(Project project);

    Project findById(int projectId);

    List<Project> findAll(int userId, int limit, int page);

    void delete(int projectId);


}
