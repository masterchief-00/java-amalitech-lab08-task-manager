package com.kwizera.controllers;

import com.kwizera.Exceptions.DuplicateEmailException;
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

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private EmployeeServices employeeServices;
    private DataSource dataSource;

    @Override
    public void init() {
        dataSource = (DataSource) getServletContext().getAttribute("DATA_SOURCE");
        this.employeeServices = new EmployeeServicesImpl(new EmployeeDAOImpl(dataSource));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (InputValidationUtil.invalidNames(firstName) || InputValidationUtil.invalidNames(lastName)) {
            CustomLogger.log(CustomLogger.LogLevel.WARN, "Registration failed, Invalid names");
            request.setAttribute("error", "Invalid names");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
        } else if (InputValidationUtil.invalidEmail(email)) {
            CustomLogger.log(CustomLogger.LogLevel.WARN, "Registration failed, Invalid email");
            request.setAttribute("error", "Invalid email");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
        } else if (password.isBlank() || password.isEmpty()) {
            CustomLogger.log(CustomLogger.LogLevel.WARN, "Registration failed, Invalid password");
            request.setAttribute("error", "Invalid password");
            request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
        } else {
            try {
                Employee employee = new Employee(0, firstName, lastName, email, password);
                Employee registeredEmployee = employeeServices.register(employee);
                HttpSession session = request.getSession();
                HttpSessionUtil.setSession(session, registeredEmployee);
                response.sendRedirect(request.getContextPath() + "/dashboard");
            } catch (DuplicateEmailException e) {
                CustomLogger.log(CustomLogger.LogLevel.ERROR, "Registration failed, user already exists");
                request.setAttribute("error", e.getMessage());
                request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            } catch (RuntimeException e) {
                CustomLogger.log(CustomLogger.LogLevel.ERROR, "Registration failed. " + e.getMessage());
                request.setAttribute("error", e.getMessage());
                request.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(request, response);
            }
        }
    }
}
