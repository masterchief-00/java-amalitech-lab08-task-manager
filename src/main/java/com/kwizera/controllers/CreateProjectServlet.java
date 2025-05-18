package com.kwizera.controllers;

import com.kwizera.domain.dao.impl.EmployeeDAOImpl;
import com.kwizera.domain.dao.impl.ProjectDAOImpl;
import com.kwizera.domain.entities.Employee;
import com.kwizera.domain.entities.Project;
import com.kwizera.services.EmployeeServices;
import com.kwizera.services.ProjectServices;
import com.kwizera.services.impl.EmployeeServicesImpl;
import com.kwizera.services.impl.ProjectServicesImpl;
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
import java.sql.Date;
import java.time.LocalDate;

@WebServlet("/project")
public class CreateProjectServlet extends HttpServlet {
    private DataSource dataSource;
    private ProjectServices projectServices;
    private EmployeeServices employeeServices;

    @Override
    public void init() {
        dataSource = (DataSource) getServletContext().getAttribute("DATA_SOURCE");
        this.projectServices = new ProjectServicesImpl(new ProjectDAOImpl(dataSource));
        this.employeeServices = new EmployeeServicesImpl(new EmployeeDAOImpl(dataSource));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/create_project.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String due = request.getParameter("dueDate");

        if (InputValidationUtil.invalidProjectTitle(title)) {
            CustomLogger.log(CustomLogger.LogLevel.WARN, "Unable to create project, Invalid title");
            request.setAttribute("error", "Invalid names");
            request.getRequestDispatcher("/WEB-INF/views/create_project.jsp").forward(request, response);
        }
        if (InputValidationUtil.invalidProjectDescription(description)) {
            CustomLogger.log(CustomLogger.LogLevel.WARN, "Unable to create project, Invalid description");
            request.setAttribute("error", "Invalid names");
            request.getRequestDispatcher("/WEB-INF/views/create_project.jsp").forward(request, response);
        }
        if (InputValidationUtil.invalidLocalDate(due)) {
            CustomLogger.log(CustomLogger.LogLevel.WARN, "Unable to create project, Invalid due date");
            request.setAttribute("error", "Invalid due date");
            request.getRequestDispatcher("/WEB-INF/views/create_project.jsp").forward(request, response);
        }

        HttpSession session = HttpSessionUtil.getSession(request);
        if (session != null) {
            String userEmail = (String) session.getAttribute("userEmail");
            if (userEmail == null) {
                CustomLogger.log(CustomLogger.LogLevel.WARN, "Unauthorized access, session is null.");
                request.setAttribute("error", "Unauthorized access");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            }

            try {
                Employee employee = employeeServices.getEmployee(userEmail);
                Project project = new Project(0, title, description, employee, Date.valueOf(due).toLocalDate());
                projectServices.createProject(project);
                response.sendRedirect(request.getContextPath() + "/dashboard");
            } catch (RuntimeException e) {
                CustomLogger.log(CustomLogger.LogLevel.WARN, "Unable to create project, " + e.getMessage());
                request.setAttribute("error", e.getMessage());
                request.getRequestDispatcher("/WEB-INF/views/create_project.jsp").forward(request, response);
            }
        } else {
            CustomLogger.log(CustomLogger.LogLevel.WARN, "Unauthorized access, session is null.");
            request.setAttribute("error", "Unauthorized access");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}
