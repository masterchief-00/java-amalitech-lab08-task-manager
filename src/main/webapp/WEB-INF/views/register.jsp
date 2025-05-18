<%--
  Created by IntelliJ IDEA.
  User: pacst
  Date: 2025-05-18
  Time: 10:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String error = (String) request.getAttribute("error");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Register - Task Manager</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #eef1f4;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .form-box {
            background: white;
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
            width: 300px;
        }

        h2 {
            margin-bottom: 20px;
        }

        input[type=text], input[type=email], input[type=password] {
            width: 100%;
            padding: 10px;
            margin: 8px 0 16px 0;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        button {
            width: 100%;
            background-color: #28a745;
            color: white;
            padding: 10px;
            border: none;
            border-radius: 4px;
        }

        button:hover {
            background-color: #218838;
        }

        .link {
            margin-top: 10px;
            text-align: center;
        }

        .error {
            color: #ff6185;
            font-size: 15px;
        }
    </style>
</head>
<body>
<div class="form-box">
    <h2>Register</h2>
    <form action="${pageContext.request.contextPath}/register" method="post">
        <input type="text" name="firstName" placeholder="First Name" required/>
        <input type="text" name="lastName" placeholder="Last Name" required/>
        <input type="email" name="email" placeholder="Email" required/>
        <input type="password" name="password" placeholder="Password" required/>
        <button type="submit">Register</button>
    </form>
    <div class="link">
        <p>Already have an account? <a href="${pageContext.request.contextPath}/login">Login</a></p>
        <% if (error != null) {
        %>
        <label class="error">ERROR: <%=error%>
        </label>
        <%
            }
        %>
    </div>
</div>
</body>
</html>

