package com.kwizera.domain.dao;

import com.kwizera.domain.entities.Employee;

import java.sql.SQLException;

public interface EmployeeDAO {

    Employee findById(int userId);

    Employee findByEmail(String email);

    Employee save(Employee employee);
}
