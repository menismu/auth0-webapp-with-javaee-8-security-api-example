package com.auth0.examples.java;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.SessionUtils;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = {"/home"})
public class HomeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    	final String accessToken = (String) SessionUtils.get(req, "accessToken");
        final String idToken = (String) SessionUtils.get(req, "idToken");
        if (accessToken != null) {
            req.setAttribute("userId", accessToken);
        } else if (idToken != null) {
            req.setAttribute("userId", idToken);
        }
        
        req.getRequestDispatcher("/jsp/home.jsp").forward(req, res);
    }
}
