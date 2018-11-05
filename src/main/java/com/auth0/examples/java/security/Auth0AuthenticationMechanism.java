package com.auth0.examples.java.security;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
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

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import com.auth0.SessionUtils;
import com.auth0.examples.java.configuration.Auth0Configuration;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@ApplicationScoped
public class Auth0AuthenticationMechanism implements HttpAuthenticationMechanism {
	
	private static final String JWKS_KEYS_KEY = "keys";
	
	private static final String JWKS_X5C_KEY = "x5c";
	
	private static final String CERTIFICATE_INSTANCE = "X.509";
	
	private static final List<String> WHITELISTED_URLS = Arrays.asList(new String[] {"/home", "/login", "/callback"});
	
	@Inject
	private Auth0Configuration auth0Configuration;
	
	@Inject
	private IdentityStore identityStore;

	@Override
	public AuthenticationStatus validateRequest(HttpServletRequest request, HttpServletResponse response,
			HttpMessageContext httpMessageContext) throws AuthenticationException {
		
		if (WHITELISTED_URLS.stream().anyMatch(s -> request.getRequestURI().contains(s))) {
			
        	return httpMessageContext.doNothing();
		}
		
		String authorizationHeader = (String) SessionUtils.get(request, "idToken");
		if (authorizationHeader != null) {
			
            try {
                // request public key to Auth0
                HttpResponse<String> jwks = Unirest.get("https://" + auth0Configuration.getDomain() + "/.well-known/jwks.json")
    					  .header("content-type", "application/json")
    					  .asString();
                
                JSONObject jsonJwks = new JSONObject(jwks.getBody());
                String x5cBase64 = ((JSONObject) jsonJwks.getJSONArray(JWKS_KEYS_KEY)
                		.get(0))
                		.getJSONArray(JWKS_X5C_KEY)
                		.getString(0);
                byte[] x5cBytes = Base64.decodeBase64(x5cBase64);
                
            	// get public key 
                CertificateFactory fact = CertificateFactory.getInstance(CERTIFICATE_INSTANCE);
                X509Certificate cer = (X509Certificate) fact.generateCertificate(new ByteArrayInputStream(x5cBytes));
                RSAPublicKey publicKey = (RSAPublicKey) cer.getPublicKey();
                RSAPrivateKey privateKey = null;
                
                // verify token
                Algorithm algorithm = Algorithm.RSA256(publicKey, privateKey);
                JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("https://" + auth0Configuration.getDomain() + "/")
                    .build();
                
                verifier.verify(authorizationHeader);
            } catch (JWTVerificationException | CertificateException | UnirestException exception){
            	
            	// return unauthorized if any problem occurs verifying token
            	return httpMessageContext.responseUnauthorized();
            }
            
            Credential credential = new Auth0Credential(authorizationHeader, new HashSet<String>(Arrays.asList("MEMBER")));

            // token valid, validating credential
    		return httpMessageContext.notifyContainerAboutLogin(identityStore.validate(credential));
        } else {
        	
        	return httpMessageContext.responseUnauthorized();
        }
	}
}
