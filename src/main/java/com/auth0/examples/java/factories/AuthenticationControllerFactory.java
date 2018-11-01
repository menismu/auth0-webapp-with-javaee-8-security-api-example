package com.auth0.examples.java.factories;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import com.auth0.AuthenticationController;
import com.auth0.examples.java.configuration.Auth0Configuration;

@ApplicationScoped
public class AuthenticationControllerFactory {

	@Produces
	public AuthenticationController authenticationController(Auth0Configuration auth0Configuration) {
		
		return AuthenticationController.newBuilder(auth0Configuration.getDomain(), auth0Configuration.getClientId(),
				auth0Configuration.getClientSecret()).build();
	}
}
