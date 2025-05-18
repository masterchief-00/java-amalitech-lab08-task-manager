<%--
  Created by IntelliJ IDEA.
  User: pacst
  Date: 2025-05-18
  Time: 14:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.kwizera.domain.entities.Project" %>
<%@ page import="com.kwizera.domain.entities.Task" %>

<%
    Project project = (Project) request.getAttribute("project");
    List<Task> tasks = (List<Task>) request.getAttribute("tasks");
%>

<!DOCTYPE html>
<html>
<head>
    <title><%= project.getTitle() %> - Tasks</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f2f3f7;
            padding: 20px;
        }

        h1 {
            color: #333;
        }

        .project-details {
            margin-bottom: 20px;
        }

        .task-list {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 20px;
        }

        .task-card {
            background: white;
            border-radius: 8px;
            padding: 16px;
            width: 300px;
            box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
        }

        .task-title {
            font-size: 1.2em;
            font-weight: bold;
            margin-bottom: 10px;
        }

        .task-meta {
            font-size: 0.9em;
            color: #666;
            margin-bottom: 4px;
        }

        .add-task-btn {
            margin-top: 20px;
            background-color: #28a745;
            color: white;
            padding: 10px 16px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
            display: inline-block;
        }

        .add-task-btn:hover {
            background-color: #218838;
        }

        .task-brief {
            font-size: 14px;
            color: #2c2c2c;
        }

        .task-header {
            display: flex;
            align-items: center;
            flex-direction: row;
            gap: 5px;
        }

        .task-edit-btn {
            background-color: #008dff;
            color: white;
            font-size: 14px;
            font-weight: bold;
            padding: 5px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
        }

        .title-container {
            display: flex;
            flex-direction: row;
            align-items: center;
            gap: 15px;
        }

        .go-back {
            background-color: #008dff;
            color: white;
            font-size: 14px;
            font-weight: bold;
            padding: 5px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            text-decoration: none;
        }
    </style>
</head>
<body>

<div class="project-details">
    <div class="title-container">
        <h1>Project: <%= project.getTitle() %>
        </h1>
        <a class="go-back" href="${pageContext.request.contextPath}/dashboard">All Projects</a>
    </div>

    <p><strong>Description:</strong> <%= project.getDescription() %>
    </p>
    <p><strong>Due Date:</strong> <%= project.getDue() %>
    </p>
    <a href="${pageContext.request.contextPath}/task/create?projectId=<%= project.getId() %>" class="add-task-btn">+
        Create Task</a>
</div>

<div class="task-list">
    <%
        if (tasks != null && !tasks.isEmpty()) {
            for (Task task : tasks) {
    %>
    <div class="task-card">
        <div class="task-header">
            <div class="task-title"><%= task.getTitle() %>
            </div>
            <a class="task-edit-btn"
               href="${pageContext.request.contextPath}/task/update?taskId=<%= task.getId() %>&projectId=<%=project.getId()%>">Update</a>
        </div>
        <div class="task-meta">Priority: <%= task.getPriority() %>
        </div>
        <div class="task-meta">Status: <%= task.getStatus() %>
        </div>
        <div class="task-meta">Due: <%= task.getDue() %>
        </div>
        <p class="task-brief">
            <%= task.getDescription() %>
        </p>
    </div>
    <%
        }
    } else {
    %>
    <p>No tasks available for this project.</p>
    <%
        }
    %>
</div>

</body>
</html>

