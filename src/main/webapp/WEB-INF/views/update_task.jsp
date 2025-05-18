<%@ page import="com.kwizera.domain.entities.Task" %><%--
  Created by IntelliJ IDEA.
  User: pacst
  Date: 2025-05-18
  Time: 21:04
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String error = (String) request.getAttribute("error");
    String projectId = request.getParameter("projectId");
    String taskId;
    if (request.getParameter("taskId") == null) {
        taskId = (String) request.getAttribute("taskId");
    } else {
        taskId = request.getParameter("taskId");
    }

    Task task = (Task) request.getAttribute("task");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Create Task</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f7f8fa;
            padding: 20px;
        }

        h1 {
            color: #333;
        }

        form {
            background: white;
            padding: 20px;
            max-width: 500px;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        }

        label {
            display: block;
            margin-top: 12px;
            font-weight: bold;
        }

        input[type="text"],
        textarea,
        select,
        input[type="date"] {
            width: 100%;
            padding: 8px;
            margin-top: 6px;
            border: 1px solid #ccc;
            border-radius: 4px;
            box-sizing: border-box;
        }

        textarea {
            resize: vertical;
            height: 100px;
        }

        button {
            margin-top: 20px;
            background-color: #007bff;
            color: white;
            padding: 10px 18px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }

        button:hover {
            background-color: #0069d9;
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

        .error {
            color: #ff6185;
            font-size: 15px;
            font-weight: normal;
        }

        .form-footer {
            display: flex;
            flex-direction: row;
            justify-content: space-between;
            align-items: center;
            width: 100%;
            gap: 15px;
        }
    </style>
</head>
<body>
<div class="title-container">
    <h1>Update task</h1>
    <a class="go-back" href="${pageContext.request.contextPath}/task?projectId=<%= projectId %>">Back to project</a>
</div>

<%
    if (task != null) {
%>

<form action="${pageContext.request.contextPath}/task/update" method="post">
    <input type="hidden" name="taskId" value="<%= taskId %>">
    <input type="hidden" name="projectId" value="<%= projectId %>">

    <label for="title">Title</label>
    <input type="text" name="title" id="title" value="<%= task.getTitle() %>" required>

    <label for="description">Description</label>
    <textarea name="description" id="description" required><%= task.getDescription() %></textarea>

    <label for="priority">Priority</label>
    <select name="priority" id="priority" required>
        <option value="HIGH" <%= "HIGH".equals(task.getPriority().toString()) ? "selected" : ""%>>High</option>
        <option value="MEDIUM" <%= "MEDIUM".equals(task.getPriority().toString()) ? "selected" : ""%>>Medium</option>
        <option value="LOW" <%= "LOW".equals(task.getPriority().toString()) ? "selected" : ""%>>Low</option>
    </select>

    <label for="status">Status</label>
    <select name="status" id="status" required>
        <option value="PENDING" <%= "PENDING".equals(task.getStatus().toString()) ? "selected" : ""%>>Pending</option>
        <option value="COMPLETED" <%= "COMPLETE".equals(task.getStatus().toString()) ? "selected" : ""%>>Completed
        </option>
        <option value="CANCELLED" <%= "CANCELLED".equals(task.getPriority().toString()) ? "selected" : ""%>>Cancelled
        </option>
    </select>

    <label for="dueDate">Due Date</label>
    <input type="date" name="dueDate" id="dueDate" value="<%= task.getDue() %>" required>
    <div class="form-footer">
        <button type="submit">Update Task</button>
        <% if (error != null) {
        %>
        <label class="error">ERROR: <%=error%>
        </label>
        <%
        } else {%>
        <div></div>
        <%
            }%>
    </div>

</form>
<%
} else {
%>
<label class="error">ERROR: <%=error%><label>
        <%
        }
        %>
</body>
</html>
