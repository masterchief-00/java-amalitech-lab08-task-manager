package com.kwizera.controllers;

import com.kwizera.domain.dao.impl.ProjectDAOImpl;
import com.kwizera.domain.entities.Project;
import com.kwizera.services.ProjectServices;
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
import java.util.List;

@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {
    private DataSource dataSource;
    private ProjectServices projectServices;

    @Override
    public void init() {
        dataSource = (DataSource) getServletContext().getAttribute("DATA_SOURCE");
        this.projectServices = new ProjectServicesImpl(new ProjectDAOImpl(dataSource));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = HttpSessionUtil.getSession(request);
        if (session != null) {
            Integer userId = (Integer) session.getAttribute("userId");
            if (userId == null) {
                CustomLogger.log(CustomLogger.LogLevel.WARN, "Unauthorized access, session is null.");
                request.setAttribute("error", "Unauthorized access");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            }

            try {
                List<Project> projects = projectServices.getProjects(userId, 10, 1);
                CustomLogger.log(CustomLogger.LogLevel.INFO, projects.size() + " projects retrieved");
                request.setAttribute("projects", projects);
                request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
            } catch (RuntimeException e) {
                CustomLogger.log(CustomLogger.LogLevel.ERROR, e.getMessage());
                request.setAttribute("error", "Unable to load projects. " + e.getMessage());
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            }

        } else {
            CustomLogger.log(CustomLogger.LogLevel.WARN, "Unauthorized access, session is null.");
            request.setAttribute("error", "Unauthorized access");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idParam = request.getParameter("projectId");
        if (idParam == null || InputValidationUtil.invalidURLParam(idParam)) {
            CustomLogger.log(CustomLogger.LogLevel.WARN, "Invalid URL param. project not deleted");
            request.setAttribute("error", "Invalid project id");
            request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
        }

        try {
            projectServices.delete(Integer.parseInt(idParam));
            response.sendRedirect(request.getContextPath() + "/dashboard");
        } catch (RuntimeException e) {
            CustomLogger.log(CustomLogger.LogLevel.ERROR, e.getMessage());
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
        }
    }
}
