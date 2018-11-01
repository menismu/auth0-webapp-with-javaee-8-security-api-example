package com.auth0.examples.java.configuration;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class Auth0Configuration {
	
	@Inject
	@Property("auth0.domain")
	private String domain;
	
	@Inject
    @Property("auth0.client.id")
	private String clientId;
	
	@Inject
	@Property("auth0.client.secret")
	private String clientSecret;
	
	public String getDomain() {
		return domain;
	}
	
	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
}
