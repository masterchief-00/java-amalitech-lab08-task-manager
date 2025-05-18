# LAB: Task Management System

## Overview
This is a simple task management web application built using Java Servlets, JSP, JDBC, and PostgreSQL. It supports user authentication, project and task management, with support for priorities, statuses, due dates, and session-based access control.

## 🛠 Features

- User registration and login with hashed passwords
- Session-based authentication
- Project and task management per user
- Task priority and status enums
- Task deadlines and overdue tracking
- MVC-like structure with servlets as controllers
- JSPs stored under `WEB-INF` for security
- Basic service layer for clean business logic separation

## ▱ ER Diagram
![erd.png](src%2Fmain%2Fresources%2Ferd.png)

## ⚙️ Database

Sample tables (simplified):

- `employees (id, first_name,last_name,email, password)`
- `projects (id, title, description, employee_id, due, created_at, updated_at)`
- `tasks (id, title, description, employee_id, due priority, status)`

Indexes are added for performance on foreign keys and frequent queries.

## 💻 Technologies Used

- Java 17
- Servlet API
- JSP
- JDBC
- PostgreSQL
- Apache Tomcat 11
- Maven

## How to run

- Clone the project
- Maven adds dependencies automatically
- Make sure to have a postgres instance running
- create a database, call it `tasks_db` to match the hard coded connection string
- Run the Tomcat
- 👍