package com.auth0.examples.java.security;

import java.io.ByteArrayInputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

import com.auth0.examples.java.configuration.Auth0Configuration;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

@ApplicationScoped
public class Auth0TokenValidator {
	
	private static final String JWKS_KEYS_KEY = "keys";
	
	private static final String JWKS_X5C_KEY = "x5c";
	
	private static final String CERTIFICATE_INSTANCE = "X.509";
	
	@Inject
	private Auth0Configuration auth0Configuration;

	public void validate(String token) throws UnirestException, CertificateException {
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
        
        verifier.verify(token);
	}
}
