package com.kwizera.controllers;

import com.kwizera.domain.dao.impl.ProjectDAOImpl;
import com.kwizera.domain.dao.impl.TaskDAOImpl;
import com.kwizera.domain.entities.Project;
import com.kwizera.domain.entities.Task;
import com.kwizera.domain.entities.TaskPriority;
import com.kwizera.domain.entities.TaskStatus;
import com.kwizera.services.ProjectServices;
import com.kwizera.services.TaskServices;
import com.kwizera.services.impl.ProjectServicesImpl;
import com.kwizera.services.impl.TaskServicesImpl;
import com.kwizera.utils.CustomLogger;
import com.kwizera.utils.InputValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Date;

@WebServlet("/task/update")
public class UpdateTaskServlet extends HttpServlet {
    private DataSource dataSource;
    private TaskServices taskServices;
    private ProjectServices projectServices;

    @Override
    public void init() {
        dataSource = (DataSource) getServletContext().getAttribute("DATA_SOURCE");
        this.taskServices = new TaskServicesImpl(new TaskDAOImpl(dataSource));
        this.projectServices = new ProjectServicesImpl(new ProjectDAOImpl(dataSource));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String taskId;
        if (request.getParameter("taskId") == null) {
            taskId = (String) request.getAttribute("taskId");
        } else {
            taskId = request.getParameter("taskId");
        }

        if (taskId == null || InputValidationUtil.invalidURLParam(taskId)) {
            CustomLogger.log(CustomLogger.LogLevel.WARN, "No task selected");
            request.setAttribute("error", "Unauthorized access");
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } else {
            try {
                Task task = taskServices.findTask(Integer.parseInt(taskId));
                request.setAttribute("task", task);
                request.setAttribute("projectId", task.getProject().getId());
                request.getRequestDispatcher("/WEB-INF/views/update_task.jsp").forward(request, response);
            } catch (RuntimeException e) {
                CustomLogger.log(CustomLogger.LogLevel.ERROR, "Unable to load task details, " + e.getMessage());
                request.setAttribute("error", e.getMessage());
                request.setAttribute("taskId", taskId);
                request.getRequestDispatcher("/WEB-INF/views/update_task.jsp").forward(request, response);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String taskId = request.getParameter("taskId");
        String projectId = request.getParameter("projectId");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String priority = request.getParameter("priority");
        String status = request.getParameter("status");
        String dueDate = request.getParameter("dueDate");

        if (InputValidationUtil.invalidProjectTitle(title)) {
            CustomLogger.log(CustomLogger.LogLevel.WARN, "Unable to update task, Invalid title");
            request.setAttribute("error", "Invalid title");
            request.setAttribute("taskId", taskId);
            request.getRequestDispatcher("/WEB-INF/views/update_task.jsp").forward(request, response);
        } else if (InputValidationUtil.invalidProjectDescription(description)) {
            CustomLogger.log(CustomLogger.LogLevel.WARN, "Unable to update task, Invalid description");
            request.setAttribute("error", "Invalid description");
            request.setAttribute("taskId", taskId);
            request.getRequestDispatcher("/WEB-INF/views/update_task.jsp").forward(request, response);
        } else if (InputValidationUtil.invalidLocalDate(dueDate)) {
            CustomLogger.log(CustomLogger.LogLevel.WARN, "Unable to update task, Invalid due date");
            request.setAttribute("error", "Invalid due date");
            request.setAttribute("taskId", taskId);
            request.getRequestDispatcher("/WEB-INF/views/update_task.jsp").forward(request, response);
        } else {
            try {
                TaskPriority.valueOf(priority);
            } catch (IllegalArgumentException e) {
                CustomLogger.log(CustomLogger.LogLevel.WARN, "Unable to update task, Invalid priority value");
                request.setAttribute("error", "Invalid priority value");
                request.setAttribute("taskId", taskId);
                request.getRequestDispatcher("/WEB-INF/views/update_task.jsp").forward(request, response);
            }

            try {
                TaskStatus.valueOf(status);
            } catch (IllegalArgumentException e) {
                CustomLogger.log(CustomLogger.LogLevel.WARN, "Unable to update task, Invalid status value. " + e.getMessage());
                request.setAttribute("error", "Invalid status value");
                request.setAttribute("taskId", taskId);
                request.getRequestDispatcher("/WEB-INF/views/update_task.jsp").forward(request, response);
            }

            try {
                Task currentTask = taskServices.findTask(Integer.parseInt(taskId));
                Project project = projectServices.getProject(currentTask.getProject().getId());
                Task newTask = new Task(currentTask.getId(), title, description, TaskPriority.valueOf(priority), Date.valueOf(dueDate).toLocalDate(), TaskStatus.valueOf(status), project);
                taskServices.updateTask(newTask);
                response.sendRedirect(request.getContextPath() + "/task?projectId=" + projectId);
            } catch (RuntimeException e) {
                CustomLogger.log(CustomLogger.LogLevel.WARN, e.getMessage());
                request.setAttribute("error", "Unable to update task");
                request.setAttribute("taskId", taskId);
                request.getRequestDispatcher("/WEB-INF/views/update_task.jsp").forward(request, response);
            }
        }

    }

}
