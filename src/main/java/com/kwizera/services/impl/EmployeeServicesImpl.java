package com.kwizera.services.impl;

import com.kwizera.Exceptions.DuplicateEmailException;
import com.kwizera.domain.dao.EmployeeDAO;
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

    @Override
    public Employee register(Employee employee) throws DuplicateEmailException {
        Employee foundEmployee = employeeDAO.findByEmail(employee.getEmail());
        if (foundEmployee != null) {
            throw new DuplicateEmailException("The provided email is already registered");
        }
        employee.setPassword(PasswordUtil.hashPassword(employee.getPassword()));
        return employeeDAO.save(employee);
    }

    @Override
    public Employee getEmployee(String email) {
        return employeeDAO.findByEmail(email);
    }
}
