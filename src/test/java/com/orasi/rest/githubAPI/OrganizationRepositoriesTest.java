package com.orasi.rest.githubAPI;

import java.io.IOException;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.orasi.api.restServices.core.RestService_V2;
import com.orasi.api.restServices.services.gitHub.OrganizationRepositories;
import com.orasi.api.restServices.services.gitHub.User;

public class OrganizationRepositoriesTest {
	
	@Test
	public void gitHubUserTest() throws ClientProtocolException, IOException{
		String gitHubOrg = "orasi";
		//instantiate the base rest service class
		RestService_V2 restService = new RestService_V2();
		//send in the get request
		restService.sendGetRequest("https://api.github.com/orgs/" + gitHubOrg + "/repos");
		//verify request comes back as 200 ok
		Assert.assertEquals(restService.getStatusCode(), HttpStatus.SC_OK);
		//verify format response is json
		Assert.assertEquals(restService.getResponseFormat(), "json");
		//take the json response string and transform to java object
		OrganizationRepositories repos = restService.mapJSONToObject(OrganizationRepositories.class);
		
		//possible validations?
		//maybe assert that the values returned are what we expect?  could still data drive that,
		//also could possibly just do regex validations
		System.out.println(repos.getId());
		System.out.println(repos.getName());
		Assert.assertTrue(repos.getName().equalsIgnoreCase(gitHubOrg));
		


	   
       
	}

}
