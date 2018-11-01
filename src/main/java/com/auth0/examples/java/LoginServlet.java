package com.auth0.examples.java;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.AuthenticationController;
import com.auth0.examples.java.configuration.Auth0Configuration;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {
	
	@Inject
	private AuthenticationController authenticationController;
	
	@Inject
	private Auth0Configuration auth0Configuration;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    	String redirectUri = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath() + "/callback";

        String authorizeUrl = authenticationController.buildAuthorizeUrl(req, redirectUri)
                .withAudience(String.format("https://%s/userinfo", auth0Configuration.getDomain()))
                .build();
        res.sendRedirect(authorizeUrl);
    }
}

