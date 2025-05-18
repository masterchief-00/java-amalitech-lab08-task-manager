package com.kwizera.controllers;

import com.kwizera.domain.dao.impl.ProjectDAOImpl;
import com.kwizera.domain.dao.impl.TaskDAOImpl;
import com.kwizera.domain.entities.Project;
import com.kwizera.domain.entities.Task;
import com.kwizera.services.ProjectServices;
import com.kwizera.services.TaskServices;
import com.kwizera.services.impl.ProjectServicesImpl;
import com.kwizera.services.impl.TaskServicesImpl;
import com.kwizera.utils.CustomLogger;
import com.kwizera.utils.HttpSessionUtil;
import com.kwizera.utils.InputValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;

@WebServlet("/task")
public class TaskServlet extends HttpServlet {
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

        String projectId = request.getParameter("projectId");
        if (projectId == null || InputValidationUtil.invalidURLParam(projectId)) {
            CustomLogger.log(CustomLogger.LogLevel.WARN, "No project selected");
            request.setAttribute("error", "Unauthorized access");
            response.sendRedirect(request.getContextPath() + "/dashboard");
        }

        try {
            int projectIdParsed = Integer.parseInt(projectId);
            List<Task> tasks = taskServices.getAllTasks(projectIdParsed, 10, 1);
            CustomLogger.log(CustomLogger.LogLevel.INFO, tasks.size() + " tasks retrieved");
            request.setAttribute("tasks", tasks);

            Project project = projectServices.getProject(projectIdParsed);
            request.setAttribute("project", project);
            request.getRequestDispatcher("/WEB-INF/views/project.jsp").forward(request, response);
        } catch (RuntimeException e) {
            CustomLogger.log(CustomLogger.LogLevel.ERROR, e.getMessage());
            request.setAttribute("error", "Unable to load tasks. " + e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/project.jsp").forward(request, response);
        }
    }

}
