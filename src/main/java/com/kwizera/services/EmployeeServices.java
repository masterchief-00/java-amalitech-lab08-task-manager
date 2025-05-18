package com.kwizera.services;

import com.kwizera.Exceptions.DuplicateEmailException;
import com.kwizera.domain.entities.Employee;

public interface EmployeeServices {
    Employee login(String email, String password);

    Employee register(Employee employee) throws DuplicateEmailException;

    Employee getEmployee(String email);
}
