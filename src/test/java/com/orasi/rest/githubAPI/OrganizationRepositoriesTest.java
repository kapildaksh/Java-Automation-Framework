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
	
	/** Playing around with different ways of testing this service, map each root node to a hash map
	 * 	, take each root node and make a Jackson tree of it.
	 *	With the last option, you could take the tree for each root node, find the repo you're interested in,
	 *	then do something with it?  Maybe get the listing of teams for this repo using teams_url, have it in 
	 *	a variable and save for another test to send in another request? 
	 *
	 */
	
	String teamsURL=null;
	@Test
	public void orgReposTreeTest() throws ClientProtocolException, IOException{
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
	    	   
	    	   System.out.println(repoNode.path("name").asText());
	    	   
	    	   
	    	   if (repoNode.path("name").asText().equalsIgnoreCase("java-automation-framework")) {
	    		   teamsURL = repoNode.path("teams_url").asText();
	    		   break;
	    	   }
	    	   
	    	   
	     }
 
		
		response = restService.sendGetRequest(teamsURL);
		//verify request comes back as 200 ok
		Assert.assertEquals(restService.getStatusCode(), HttpStatus.SC_OK);
		//verify format response is json
		Assert.assertEquals(restService.getResponseFormat(), "json");
		node = restService.mapJSONToTree();
		
	}

}
