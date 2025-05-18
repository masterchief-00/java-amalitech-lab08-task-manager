package com.kwizera.controllers;

import com.kwizera.domain.dao.impl.ProjectDAOImpl;
import com.kwizera.domain.dao.impl.TaskDAOImpl;
import com.kwizera.domain.entities.*;
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
import jakarta.servlet.http.HttpSession;

import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Date;

@WebServlet("/task/create")
public class CreateTaskServlet extends HttpServlet {
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
        request.getRequestDispatcher("/WEB-INF/views/create_task.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String projectId = request.getParameter("projectId");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String priority = request.getParameter("priority");
        String status = request.getParameter("status");
        String dueDate = request.getParameter("dueDate");

        if (InputValidationUtil.invalidProjectTitle(title)) {
            CustomLogger.log(CustomLogger.LogLevel.WARN, "Unable to create task, Invalid title");
            request.setAttribute("error", "Invalid title");
            request.getRequestDispatcher("/WEB-INF/views/create_task.jsp").forward(request, response);
        } else if (InputValidationUtil.invalidProjectDescription(description)) {
            CustomLogger.log(CustomLogger.LogLevel.WARN, "Unable to create task, Invalid description");
            request.setAttribute("error", "Invalid description");
            request.getRequestDispatcher("/WEB-INF/views/create_task.jsp").forward(request, response);
        } else if (InputValidationUtil.invalidLocalDate(dueDate)) {
            CustomLogger.log(CustomLogger.LogLevel.WARN, "Unable to create task, Invalid due date");
            request.setAttribute("error", "Invalid due date");
            request.getRequestDispatcher("/WEB-INF/views/create_task.jsp").forward(request, response);
        } else {
            try {
                TaskPriority.valueOf(priority);
            } catch (IllegalArgumentException e) {
                CustomLogger.log(CustomLogger.LogLevel.WARN, "Unable to create task, Invalid priority value");
                request.setAttribute("error", "Invalid priority value");
                request.getRequestDispatcher("/WEB-INF/views/create_task.jsp").forward(request, response);
            }

            try {
                TaskStatus.valueOf(status);
            } catch (IllegalArgumentException e) {
                CustomLogger.log(CustomLogger.LogLevel.WARN, "Unable to create task, Invalid status value");
                request.setAttribute("error", "Invalid status value");
                request.getRequestDispatcher("/WEB-INF/views/create_task.jsp").forward(request, response);
            }

            if (projectId.isEmpty() || InputValidationUtil.invalidURLParam(projectId)) {
                CustomLogger.log(CustomLogger.LogLevel.WARN, "Unable to create task. Invalid project selected");
                request.setAttribute("error", "Invalid project selected");
                request.getRequestDispatcher("/WEB-INF/views/create_task.jsp").forward(request, response);
            }

            try {
                Project project = projectServices.getProject(Integer.parseInt(projectId));
                Task newTask = new Task(0, title, description, TaskPriority.valueOf(priority), Date.valueOf(dueDate).toLocalDate(), TaskStatus.valueOf(status), project);
                Task createdTask = taskServices.createTask(newTask);
                response.sendRedirect(request.getContextPath() + "/task?projectId=" + projectId);
            } catch (RuntimeException e) {
                CustomLogger.log(CustomLogger.LogLevel.WARN, e.getMessage());
                request.setAttribute("error", "Unable to create task");
                request.getRequestDispatcher("/WEB-INF/views/create_task.jsp").forward(request, response);
            }
        }

    }
}
