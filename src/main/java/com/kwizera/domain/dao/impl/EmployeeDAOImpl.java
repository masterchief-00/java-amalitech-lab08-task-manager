package com.kwizera.domain.dao.impl;

import com.kwizera.domain.dao.EmployeeDAO;
import com.kwizera.domain.entities.Employee;
import com.kwizera.utils.CustomLogger;
import jakarta.servlet.ServletContextEvent;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeDAOImpl implements EmployeeDAO {
    private final DataSource dataSource;

    public EmployeeDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Employee findById(int userId) throws RuntimeException {
        try (Connection connection = dataSource.getConnection()) {
            Employee employee = null;
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM employee WHERE id= ?")) {
                statement.setInt(1, userId);
                ResultSet rs = statement.executeQuery();

                if (rs.next()) {
                    employee = new Employee(
                            rs.getInt("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("password")
                    );
                }
                return employee;
            } catch (SQLException e) {
                CustomLogger.log(CustomLogger.LogLevel.ERROR, "Unable to find employee by id. SQLException");
                throw new RuntimeException("Unable to find employee by id.");
            }
        } catch (SQLException e) {
            CustomLogger.log(CustomLogger.LogLevel.ERROR, "Unable to establish database connection. SQLException");
            throw new RuntimeException("Unable to establish database connection.");
        }
    }

    @Override
    public Employee findByEmail(String email) throws RuntimeException {
        try (Connection connection = dataSource.getConnection()) {
            Employee employee = null;
            try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM employee WHERE email= ?")) {
                statement.setString(1, email);
                ResultSet rs = statement.executeQuery();

                if (rs.next()) {
                    employee = new Employee(
                            rs.getInt("id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email"),
                            rs.getString("password")
                    );
                }
                return employee;

            } catch (SQLException e) {
                CustomLogger.log(CustomLogger.LogLevel.ERROR, "Unable to find by email. SQLException");
                throw new RuntimeException("Unable to find by email.");
            }
        } catch (SQLException e) {
            CustomLogger.log(CustomLogger.LogLevel.ERROR, "Unable to establish database connection. SQLException");
            throw new RuntimeException("Unable to establish database connection.");
        }
    }

    @Override
    public void save(Employee employee) {
        try (Connection connection = dataSource.getConnection()) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO employee(first_name,last_name,email,password) VALUES (?,?,?,?);")) {
                preparedStatement.setString(1, employee.getFirstName());
                preparedStatement.setString(2, employee.getLastName());
                preparedStatement.setString(3, employee.getEmail());
                preparedStatement.setString(4, employee.getPassword());

                preparedStatement.executeUpdate();
                CustomLogger.log(CustomLogger.LogLevel.INFO, "Employee created");
            } catch (SQLException e) {
                CustomLogger.log(CustomLogger.LogLevel.ERROR, "Unable to create employee. SQLException");
                throw new RuntimeException("Unable to create employee.");
            }
        } catch (SQLException e) {
            CustomLogger.log(CustomLogger.LogLevel.ERROR, "Unable to establish database connection. SQLException");
            throw new RuntimeException("Unable to establish database connection.");
        }
    }
}
