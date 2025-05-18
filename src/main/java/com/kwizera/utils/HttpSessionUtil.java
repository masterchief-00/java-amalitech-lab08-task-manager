package com.kwizera.utils;

import com.kwizera.domain.entities.Employee;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class HttpSessionUtil {
    public static void setSession(HttpSession session, Employee employee) {
        session.setAttribute("userId", employee.getId());
        session.setAttribute("userEmail", employee.getEmail());
        session.setAttribute("userNames", employee.getFirstName() + " " + employee.getLastName());
    }

    public static HttpSession getSession(HttpServletRequest request) {
        return request.getSession(false);
    }
}
