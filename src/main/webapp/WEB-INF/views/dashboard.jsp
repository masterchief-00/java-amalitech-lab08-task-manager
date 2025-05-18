<%--
  Created by IntelliJ IDEA.
  User: pacst
  Date: 2025-05-17
  Time: 21:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.kwizera.domain.entities.Project" %>
<!-- Replace with your actual package -->
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dashboard - Task Manager</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f0f2f5;
            margin: 0;
            padding: 20px;
        }

        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }

        .header h2 {
            margin: 0;
        }

        .create-btn {
            background-color: #28a745;
            color: white;
            padding: 10px 20px;
            text-decoration: none;
            border-radius: 5px;
        }

        .create-btn:hover {
            background-color: #218838;
        }

        .projects {
            display: flex;
            flex-wrap: wrap;
            margin-top: 20px;
            gap: 20px;
        }

        .project-card {
            background-color: white;
            padding: 20px;
            width: 300px;
            border-radius: 8px;
            border: solid 0.5px rgba(0, 0, 0, 0.5);
            box-shadow: 0 5px 8px rgba(0, 0, 0, 0.1);
            transition: 0.2s;
            text-decoration: none;
            color: black;
        }

        .project-card:hover {
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.3);
            background-color: #f9f9f9;
        }

        .project-title {
            font-size: 1.2em;
            font-weight: bold;
        }

        .project-desc {
            margin: 10px 0;
            color: #555;
            font-size: 15px;
        }

        .due-date {
            font-size: 0.9em;
            color: #888;
        }

        .delete-btn {
            background-color: #ff6185;
            color: white;
            padding: 5px 10px;
            text-decoration: none;
            border: transparent;
            border-radius: 5px;
            margin-top: 10px;
            cursor: pointer;
        }

        .project-container {
            display: flex;
            flex-direction: column;
            gap: 6px;
            border-top: solid 8px rgba(0, 99, 248, 0.94);
            padding: 6px 0;
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
    <script>
        function confirmDelete() {
            return confirm("Are you sure you want to delete this project?");
        }
    </script>
</head>
<body>
<div class="header">
    <div class="title-container">
        <h2>Your Projects</h2>
        <a class="go-back" href="${pageContext.request.contextPath}/logout">Logout</a>
    </div>
    <a href="${pageContext.request.contextPath}/project" class="create-btn">+ New Project</a>
</div>

<div class="projects">
    <%
        List<Project> projects = (List<Project>) request.getAttribute("projects");
        if (projects != null && !projects.isEmpty()) {
            for (Project p : projects) {
    %>
    <div class="project-container">
        <a class="project-card" href="${pageContext.request.contextPath}/task?projectId=<%= p.getId() %>">
            <div class="project-title"><%= p.getTitle() %>
            </div>
            <div class="project-desc"><%= p.getDescription() %>
            </div>
            <div class="due-date">Due: <%= p.getDue() %>
            </div>
        </a>
        <form action="${pageContext.request.contextPath}/dashboard" method="post" onsubmit="return confirmDelete();">
            <input type="hidden" name="projectId" value="<%= p.getId() %>"/>
            <button class="delete-btn" type="submit">Delete project</button>
        </form>
    </div>

    <%
        }
    } else {
    %>
    <p>No projects yet. Click " New Project" to get started!</p>
    <%
        }
    %>
</div>
</body>
</html>

