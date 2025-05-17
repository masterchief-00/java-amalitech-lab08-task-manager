package com.kwizera.services.impl;

import com.kwizera.domain.dao.EmployeeDAO;
import com.kwizera.domain.dao.impl.EmployeeDAOImpl;
import com.kwizera.domain.entities.Employee;
import com.kwizera.services.EmployeeServices;
import com.kwizera.utils.PasswordUtil;

public class EmployeeServicesImpl implements EmployeeServices {
    private final EmployeeDAO employeeDAO;

    public EmployeeServicesImpl(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    @Override
    public Employee login(String email, String password) {
        Employee employee = employeeDAO.findByEmail(email);
        if (employee != null && PasswordUtil.checkPassword(password, employee.getPassword())) {
            return employee;
        }
        return null;
    }
}
