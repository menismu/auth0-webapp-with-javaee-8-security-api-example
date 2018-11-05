package com.auth0.examples.java.security;

import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.identitystore.IdentityStore;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.SessionUtils;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.mashape.unirest.http.exceptions.UnirestException;

@ApplicationScoped
public class Auth0AuthenticationMechanism implements HttpAuthenticationMechanism {
	
	private static final List<String> WHITELISTED_URLS = Arrays.asList(new String[] {"/home", "/login", "/callback"});
	
	@Inject
	private IdentityStore identityStore;
	
	@Inject
	private Auth0TokenValidator auth0TokenValidator;

	@Override
	public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response,
			HttpMessageContext httpMessageContext) throws AuthenticationException {
		
		if (WHITELISTED_URLS.stream().anyMatch(s -> request.getRequestURI().contains(s))) {
			
        	return httpMessageContext.doNothing();
		}
		
		String token = (String) SessionUtils.get(request, "idToken");
		if (token != null) {
			
            try {
                auth0TokenValidator.validate(token);
            } catch (JWTVerificationException | CertificateException | UnirestException exception){
            	
            	// return unauthorized if any problem occurs verifying token
            	return httpMessageContext.responseUnauthorized();
            }
            
            Credential credential = new Auth0Credential(token, new HashSet<String>(Arrays.asList("MEMBER")));

            // token valid, validating credential
    		return httpMessageContext.notifyContainerAboutLogin(identityStore.validate(credential));
        } else {
        	
        	return httpMessageContext.responseUnauthorized();
        }
	}
}
