package com.orasi.rest.githubAPI;

import java.io.IOException;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.orasi.api.restServices.core.RestService_V2;
import com.orasi.api.restServices.services.gitHub.User;

public class UserTest {
	
	@Test
	public void gitHubUserTest() throws ClientProtocolException, IOException{
		String gitHubUser = "jessicamarshall";
		//instantiate the base rest service class
		RestService_V2 restService = new RestService_V2();
		//send in the get request
		restService.sendGetRequest("https://api.github.com/users/" + gitHubUser);
		//verify request comes back as 200 ok
		Assert.assertEquals(restService.getStatusCode(), HttpStatus.SC_OK);
		//verify format response is json
		Assert.assertEquals(restService.getResponseFormat(), "json");
		//take the json response string and transform to java object
		User user = restService.mapJSONToObject(User.class);
		
		//possible validations?
		//maybe assert that the values returned are what we expect?  could still data drive that,
		//also could possibly just do regex validations
		System.out.println(user.getId());
		System.out.println(user.getLogin());
		Assert.assertTrue(user.getLogin().equalsIgnoreCase(gitHubUser));
	}
}
