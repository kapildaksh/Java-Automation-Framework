package com.orasi.api.restServices.services.gitHub;

//URL "user_url": "https://api.github.com/users/{user}"
public class User {
	
	private String login;
	private String id;
	
	public User() {
		super();
	}
	
	public String getLogin() {
		return login;
	}
	
	public void setLogin(final String login){
		this.login=login;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(final String id){
		this.id=id;
	}

}
