package com.kwizera.domain.dao.impl;

import com.kwizera.domain.dao.ProjectDAO;
import com.kwizera.domain.dao.TaskDAO;
import com.kwizera.domain.entities.*;
import com.kwizera.utils.CustomLogger;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TaskDAOImpl implements TaskDAO {
    private final DataSource dataSource;
    private ProjectDAO projectDAO;

    public TaskDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.projectDAO = new ProjectDAOImpl(dataSource);
    }

    @Override
    public Task findById(int taskId) throws RuntimeException {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM task WHERE id = ?")) {
                statement.setInt(1, taskId);
                ResultSet rs = statement.executeQuery();
                Task task = null;
                while (rs.next()) {
                    Project project = projectDAO.findById(rs.getInt("project_id"));
                    task = new Task(
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("description"),
                            TaskPriority.valueOf(rs.getString("priority")),
                            rs.getDate("due").toLocalDate(),
                            TaskStatus.valueOf(rs.getString("status")),
                            project
                    );
                }
                return task;
            } catch (SQLException e) {
                throw new RuntimeException("Unable to find task by id. " + e.getMessage());
            }
        } catch (SQLException e) {
            CustomLogger.log(CustomLogger.LogLevel.ERROR, "Unable to establish database connection. SQLException");
            throw new RuntimeException("Unable to establish database connection.");
        }
    }

    @Override
    public List<Task> findAll(int projectId, int limit, int page) throws RuntimeException {
        try (Connection connection = dataSource.getConnection()) {
            int page_size = limit;
            int offset = (page - 1) * page_size;
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM task WHERE project_id = ? LIMIT ? OFFSET ?;")) {
                statement.setInt(1, projectId);
                statement.setInt(2, page_size);
                statement.setInt(3, offset);
                ResultSet rs = statement.executeQuery();
                return getTasks(rs);
            } catch (SQLException e) {
                throw new RuntimeException("Unable to find tasks. " + e.getMessage());
            }
        } catch (SQLException e) {
            CustomLogger.log(CustomLogger.LogLevel.ERROR, "Unable to establish database connection. SQLException");
            throw new RuntimeException("Unable to establish database connection.");
        }
    }

    @Override
    public List<Task> priorityFiltered(int projectId, TaskPriority priority) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM task WHERE project_id = ? AND priority = ?::priority_enum")) {
                statement.setInt(1, projectId);
                statement.setString(2, priority.toString());
                ResultSet rs = statement.executeQuery();
                return getTasks(rs);
            } catch (SQLException e) {
                throw new RuntimeException("Unable to find tasks. " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to establish database connection. " + e.getMessage());
        }
    }

    @Override
    public List<Task> statusFiltered(int projectId, TaskStatus status) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM task WHERE project_id = ? AND status = ?::status_enum")) {
                statement.setInt(1, projectId);
                statement.setString(2, status.toString());
                ResultSet rs = statement.executeQuery();
                return getTasks(rs);
            } catch (SQLException e) {
                throw new RuntimeException("Unable to find tasks. " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to establish database connection. " + e.getMessage());
        }
    }

    @Override
    public List<Task> overdueFiltered(int projectId) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM task WHERE project_id = ? AND due < CURRENT_DATE")) {
                statement.setInt(1, projectId);
                ResultSet rs = statement.executeQuery();
                return getTasks(rs);
            } catch (SQLException e) {
                CustomLogger.log(CustomLogger.LogLevel.ERROR, "Unable to find tasks. SQLException");
                throw new RuntimeException("Unable to find tasks.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to establish database connection. " + e.getMessage());
        }
    }

    private List<Task> getTasks(ResultSet rs) throws SQLException {
        List<Task> tasks = new ArrayList<>();
        while (rs.next()) {
            Project project = projectDAO.findById(rs.getInt("project_id"));
            Task task = new Task(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    TaskPriority.valueOf(rs.getString("priority")),
                    rs.getDate("due").toLocalDate(),
                    TaskStatus.valueOf(rs.getString("status")),
                    project
            );
            tasks.add(task);
        }
        return tasks;
    }

    @Override
    public Task save(Task task) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("INSERT INTO task(title,description,priority,due,status,project_id) VALUES (?,?,?::priority_enum,?,?::status_enum,?)")) {
                Project project = projectDAO.findById(task.getProject().getId());

                statement.setString(1, task.getTitle());
                statement.setString(2, task.getDescription());
                statement.setString(3, task.getPriority().toString());
                statement.setDate(4, Date.valueOf(task.getDue()));
                statement.setString(5, task.getStatus().toString());
                statement.setInt(6, project.getId());
                statement.executeUpdate();
                CustomLogger.log(CustomLogger.LogLevel.INFO, "Task created");

                return task;
            } catch (SQLException e) {
                throw new RuntimeException("Unable to create task. " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to establish database connection." + e.getMessage());
        }
    }

    @Override
    public Task update(Task task) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("UPDATE task SET title=?, description=?, priority=?::priority_enum,due=?, status=?::status_enum, project_id=?,updated_at=? WHERE id=?")) {
                statement.setString(1, task.getTitle());
                statement.setString(2, task.getDescription());
                statement.setString(3, task.getPriority().toString());
                statement.setDate(4, Date.valueOf(task.getDue()));
                statement.setString(5, task.getStatus().toString());
                statement.setInt(6, task.getProject().getId());
                statement.setDate(7, Date.valueOf(LocalDate.now()));
                statement.setInt(8, task.getId());
                statement.executeUpdate();
                CustomLogger.log(CustomLogger.LogLevel.INFO, "Task updated " + task.getTitle());

                return task;
            } catch (SQLException e) {
                throw new RuntimeException("Unable to update task. " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to establish database connection." + e.getMessage());
        }
    }

    @Override
    public void delete(int taskId) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement statement = connection.prepareStatement("DELETE FROM task WHERE id = ?")) {
                statement.setInt(1, taskId);
                statement.executeQuery();
                CustomLogger.log(CustomLogger.LogLevel.INFO, "Task deleted");
            } catch (SQLException e) {
                throw new RuntimeException("Unable to delete task." + e.getMessage());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Unable to establish database connection. " + e.getMessage());
        }
    }
}
