package com.auth0.examples.java.security;

import javax.annotation.Priority;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Alternative;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;

@ApplicationScoped
@Alternative
@Priority(1)
public class Auth0IdentityStore implements IdentityStore {
	
	@Override
    public CredentialValidationResult validate(Credential credential) {
        
		if (credential instanceof Auth0Credential) {
            Auth0Credential auth0Credential = (Auth0Credential) credential;
            
            return new CredentialValidationResult(auth0Credential.getCaller(), auth0Credential.getGroups());
        }
		
        return CredentialValidationResult.INVALID_RESULT;
    }
}
