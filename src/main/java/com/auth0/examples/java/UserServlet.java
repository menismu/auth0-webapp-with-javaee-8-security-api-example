package com.auth0.examples.java;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.SessionUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = {"/user"})
public class UserServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        final String accessToken = (String) SessionUtils.get(req, "accessToken");
        
        if (accessToken != null) {
            req.setAttribute("userId", accessToken);
        }
        
        req.getRequestDispatcher("/jsp/user.jsp").forward(req, res);
    }
}

