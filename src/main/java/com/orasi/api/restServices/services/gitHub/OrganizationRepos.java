package com.orasi.api.restServices.services.gitHub;
//organization_repositories_url: "https://api.github.com/orgs/{org}/repos{?type,page,per_page,sort}",
public class OrganizationRepos {
	private String name;
	private String id;
	
	public OrganizationRepos() {
		super();
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(final String id){
		this.id=id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(final String name){
		this.name=name;
	}
}
