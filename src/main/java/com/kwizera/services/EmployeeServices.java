package com.kwizera.services;

import com.kwizera.domain.entities.Employee;

public interface EmployeeServices {
    Employee login(String email, String password);
}
