package com.kwizera.controllers;

import com.kwizera.domain.dao.impl.EmployeeDAOImpl;
import com.kwizera.domain.entities.Employee;
import com.kwizera.services.EmployeeServices;
import com.kwizera.services.impl.EmployeeServicesImpl;
import com.kwizera.utils.InputValidationUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebListener;
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (InputValidationUtil.invalidEmail(email) || password.isBlank()) {
            request.setAttribute("error", "Invalid credentials");
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }

        Employee employee = employeeServices.login(email, password);
        if (employee == null) {
            request.setAttribute("error", "User does not exists");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("userId", employee.getEmail());
            request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
        }

    }
}
