package com.kwizera.controllers;

import com.kwizera.domain.dao.impl.EmployeeDAOImpl;
import com.kwizera.domain.entities.Employee;
import com.kwizera.services.EmployeeServices;
import com.kwizera.services.impl.EmployeeServicesImpl;
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

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private EmployeeServices employeeServices;
    private DataSource dataSource;

    @Override
    public void init() {
        dataSource = (DataSource) getServletContext().getAttribute("DATA_SOURCE");
        this.employeeServices = new EmployeeServicesImpl(new EmployeeDAOImpl(dataSource));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (InputValidationUtil.invalidEmail(email) || password.isBlank()) {
            CustomLogger.log(CustomLogger.LogLevel.ERROR, "Login failed, invalid password");
            request.setAttribute("error", "Invalid credentials");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }

        try {
            Employee employee = employeeServices.login(email, password);
            if (employee == null) {
                CustomLogger.log(CustomLogger.LogLevel.ERROR, "Login failed, user does not exist");
                request.setAttribute("error", "User does not exists");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            } else {
                HttpSession session = request.getSession();
                HttpSessionUtil.setSession(session, employee);
                response.sendRedirect(request.getContextPath() + "/dashboard");
            }
        } catch (RuntimeException e) {
            CustomLogger.log(CustomLogger.LogLevel.ERROR, "Login failed, " + e.getMessage());
            request.setAttribute("error", e.getMessage());
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }

    }
}
