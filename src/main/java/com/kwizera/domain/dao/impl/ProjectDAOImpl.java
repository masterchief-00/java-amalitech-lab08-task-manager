package com.kwizera.domain.dao.impl;

import com.kwizera.domain.dao.EmployeeDAO;
import com.kwizera.domain.dao.ProjectDAO;
import com.kwizera.domain.entities.Employee;
import com.kwizera.domain.entities.Project;
import com.kwizera.utils.CustomLogger;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProjectDAOImpl implements ProjectDAO {
    private final DataSource dataSource;
    private final EmployeeDAO employeeDAO;

    public ProjectDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.employeeDAO = new EmployeeDAOImpl(dataSource);
    }

    @Override
    public void save(Project project) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO project(title,description,due,employee_id) VALUES (?,?,?,?)")) {
                Employee employee = employeeDAO.findById(project.getEmployee().getId());

                statement.setString(1, project.getTitle());
                statement.setString(2, project.getDescription());
                statement.setDate(3, Date.valueOf(project.getDue()));
                statement.setInt(4, employee.getId());
                statement.executeUpdate();
                CustomLogger.log(CustomLogger.LogLevel.INFO, "Project created");
            } catch (SQLException e) {
                CustomLogger.log(CustomLogger.LogLevel.ERROR, "Unable to create project. SQLException");
                throw new RuntimeException("Unable to create project.");
            }
        } catch (SQLException e) {
            CustomLogger.log(CustomLogger.LogLevel.ERROR, "Unable to establish database connection. SQLException");
            throw new RuntimeException("Unable to establish database connection.");
        }
    }

    @Override
    public Project findById(int projectId) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM project WHERE id = ?")) {
                Project project = null;
                statement.setInt(1, projectId);
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    Employee employee = employeeDAO.findById(rs.getInt("employee_id"));
                    LocalDate updatedAt = rs.getDate("updated_at") != null ? rs.getDate("updated_at").toLocalDate() : null;

                    project = new Project(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            employee,
                            rs.getDate("due").toLocalDate(),
                            rs.getDate("created_at").toLocalDate(),
                            Optional.ofNullable(updatedAt)
                    );
                }

                return project;
            } catch (SQLException e) {
                CustomLogger.log(CustomLogger.LogLevel.ERROR, "Unable to find project by id. " + e.getMessage());
                throw new RuntimeException("Unable to find project by id.");
            }
        } catch (SQLException e) {
            CustomLogger.log(CustomLogger.LogLevel.ERROR, "Unable to establish database connection. SQLException");
            throw new RuntimeException("Unable to establish database connection.");
        }
    }

    @Override
    public List<Project> findAll(int userId, int limit, int page) {
        try (Connection connection = dataSource.getConnection()) {
            int page_size = limit;
            int offset = (page - 1) * page_size;
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM project WHERE employee_id = ? LIMIT ? OFFSET ?")) {
                statement.setInt(1, userId);
                statement.setInt(2, page_size);
                statement.setInt(3, offset);
                ResultSet rs = statement.executeQuery();
                List<Project> projects = new ArrayList<>();

                while (rs.next()) {
                    Employee employee = employeeDAO.findById(rs.getInt("employee_id"));
                    LocalDate updatedAt = rs.getDate("updated_at") != null ? rs.getDate("updated_at").toLocalDate() : null;
                    Project project = new Project(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            employee,
                            rs.getDate("due").toLocalDate(),
                            rs.getDate("created_at").toLocalDate(),
                            Optional.ofNullable(updatedAt)
                    );
                    projects.add(project);
                }
                return projects;
            } catch (SQLException e) {
                CustomLogger.log(CustomLogger.LogLevel.ERROR, "Unable to find projects. " + e.getMessage());
                throw new RuntimeException("Unable to find projects.");
            }
        } catch (SQLException e) {
            CustomLogger.log(CustomLogger.LogLevel.ERROR, "Unable to establish database connection. SQLException");
            throw new RuntimeException("Unable to establish database connection.");
        }
    }

    @Override
    public void delete(int projectId) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM project WHERE id = ?")) {
                statement.setInt(1, projectId);
                int rowsAffected = statement.executeUpdate();
                CustomLogger.log(CustomLogger.LogLevel.INFO, rowsAffected + " projects deleted");
            } catch (SQLException e) {
                throw new RuntimeException("Unable to delete project. " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to establish database connection. " + e.getMessage());
        }
    }
}
