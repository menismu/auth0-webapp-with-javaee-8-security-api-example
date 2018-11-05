package com.auth0.examples.java.security;

import java.util.Collections;
import java.util.Set;

import javax.security.enterprise.credential.Credential;

public class Auth0Credential implements Credential {
	
	private String caller;
	
    private Set<String> groups;

	public Auth0Credential(String caller) {
        this(caller, Collections.emptySet());
    }

    public Auth0Credential(String caller, Set<String> groups) {
        this.caller = caller;
        this.groups = groups;
    }

	public String getCaller() {
		return caller;
	}

	public void setCaller(String caller) {
		this.caller = caller;
	}

	public Set<String> getGroups() {
		return groups;
	}

	public void setGroups(Set<String> groups) {
		this.groups = groups;
	}
}
