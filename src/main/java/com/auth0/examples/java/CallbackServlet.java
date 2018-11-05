package com.auth0.examples.java;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.AuthenticationController;
import com.auth0.IdentityVerificationException;
import com.auth0.SessionUtils;
import com.auth0.Tokens;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = { "/callback" })
public class CallbackServlet extends HttpServlet {
	
	@Inject
	private AuthenticationController authenticationController;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		try {
			Tokens tokens = authenticationController.handle(req);
			SessionUtils.set(req, "accessToken", tokens.getAccessToken());
			SessionUtils.set(req, "idToken", tokens.getIdToken());
			
			res.sendRedirect(req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath() + "/user");
		} catch (IdentityVerificationException e) {
			
			res.sendRedirect(req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + req.getContextPath() + "/home");
		}
	}
}
