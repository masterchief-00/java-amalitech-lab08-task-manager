package com.kwizera.domain.dao;

import com.kwizera.domain.entities.Employee;

import java.sql.SQLException;

public interface EmployeeDAO {
    Employee findByEmail(String email);

    void save(Employee employee);
}
