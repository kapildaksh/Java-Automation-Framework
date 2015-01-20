package com.orasi.api.restServices.services.gitHub;

public class Organization {
	private String login;
	private String id;
	
	public Organization() {
		super();
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(final String login){
		this.login=login;
	}
}
