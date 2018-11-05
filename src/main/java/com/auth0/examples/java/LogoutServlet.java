package com.auth0.examples.java;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    	
    	if (req.getSession() != null) {
            req.getSession().invalidate();
        }
    	
    	res.sendRedirect(req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath() + "/home");
    }
}

