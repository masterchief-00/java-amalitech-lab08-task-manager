package com.kwizera.controllers;

import com.kwizera.utils.HttpSessionUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/logout")
public class LogOutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSessionUtil.destroySession(request);
        response.sendRedirect(request.getContextPath() + "/login");
    }
}
