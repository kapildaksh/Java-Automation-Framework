package com.orasi.rest.githubAPI;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orasi.api.restServices.core.RestService_V2;
import com.orasi.api.restServices.services.gitHub.OrganizationRepos;
import com.orasi.api.restServices.services.gitHub.User;

public class OrganizationRepositoriesTest {
	
	@Test
	public void gitHubUserTest() throws ClientProtocolException, IOException{
		String gitHubOrg = "orasi";
		//instantiate the base rest service class
		RestService_V2 restService = new RestService_V2();
		//send in the get request
		String response = restService.sendGetRequest("https://api.github.com/orgs/" + gitHubOrg + "/repos");
		//verify request comes back as 200 ok
		Assert.assertEquals(restService.getStatusCode(), HttpStatus.SC_OK);
		//verify format response is json
		Assert.assertEquals(restService.getResponseFormat(), "json");
		
		//create a tree of json response
		JsonNode node = restService.mapJSONToTree();
		
		//create an iterator of all the repo nodes
		Iterator<JsonNode> nodeIterator = node.iterator();
		
		ObjectMapper mapper = new ObjectMapper().
			      configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		//display all the repo nodes and their type (search results) for this organization
		 while (nodeIterator.hasNext()) {
	    	   JsonNode rootNode = nodeIterator.next();
	           // what is its type
	           //System.out.println(rootNode.getNodeType());// Prints Object
	    	   //System.out.println(rootNode.toString()); 
	    	   
	    	   //convert each root node to a map
	    	   Map<String,String> myMap = new HashMap<String, String>();
	    	   myMap = mapper.readValue(rootNode.toString(), 
	    			   HashMap.class);
	    	   //print out the entire map
	    	   System.out.println(myMap);
	    	   
	    	   //convert each root node into its own tree
	    	   JsonNode repoNode = mapper.readTree(rootNode.toString());
	    	   System.out.println(repoNode.path("name"));
	    	   System.out.println(repoNode.path("language"));
	    	   System.out.println(repoNode.path("teams_url"));
	    	   
	     }
		 
		 //take each repo node (they are all the same), and create a class for each
		 
		
		
		/*
		OrganizationRepositories[] repos = response.
		OrganizationRepositories repos = restService.mapJSONToObject(OrganizationRepositories.class);
		
		//possible validations?
		//maybe assert that the values returned are what we expect?  could still data drive that,
		//also could possibly just do regex validations
		System.out.println(repos.getId());
		System.out.println(repos.getName());
		Assert.assertTrue(repos.getName().equalsIgnoreCase(gitHubOrg));
		*/
		


	   
       
	}

}
