/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.rest.misc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orasi.arven.sandbox.rest.MockMicroblogServer;
import com.orasi.utils.rest.PostmanCollection;
import com.orasi.utils.rest.PostmanEnvironment;
import com.orasi.utils.rest.RestCollection;
import com.orasi.utils.rest.RestRequest;
import com.squareup.okhttp.Response;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.testng.Assert;

/**
 * @author Brian Becker
 */
public class MicroblogCucumberStepdefs {
    
    public static final String REST_SANDBOX = "/rest/sandbox/";
    public static final MockMicroblogServer server = new MockMicroblogServer();
    
    static {
        server.start();
    }
    
    public ObjectMapper map;
    public RestCollection collection;
    public Map env1;
    public Map env2;
    
    private RestRequest request = null;
    private final Set<String> created = new HashSet<String>();
    
    public MicroblogCucumberStepdefs() throws Exception {
        collection = PostmanCollection.file(getClass().getResource(REST_SANDBOX + "MicroBlog.json.postman_collection"));
        env1 = PostmanEnvironment.file(getClass().getResource(REST_SANDBOX + "Passwords.postman_environment"));
        env2 = new HashMap();
        env2.put("username", "arven2");
        env2.put("nickname", "A. R. Variadic");
        env2.put("email", "arvariadic@arven.info");        
        collection.withEnv(env1);
    }
    
    @Given("^I am not logged in$")
    public void I_am_not_logged_in() {
        // Do nothing
    }
    
    @Given("^I am logged in as (.*)$")
    public void I_am_logged_in_as(String label) {
        // Do nothing
    }
    
    @And("^(.*)'s account has been created$")
    public void account_has_been_created(String label) {
        // Assert.assertTrue(created.contains(label));
    }

    @And("^(.*)'s account has not been created$")
    public void account_has_not_been_created(String label) {
        // Assert.assertFalse(created.contains(label));
    }
    
    @When("^I send a request to (.*)$")
    public void I_send_a_request_to(String label) throws Throwable {
        request = collection.byName(label);
    }
    
    @Then("^I expect a response matching (.*)$")
    public void I_expect_a_response_matching(String response) throws Throwable {
        request.response(response).validate();
    }
    
    @Then("^I expect the response to succeed$")
    public void I_expect_the_response_to_succeed() throws Throwable {
        Response r = request.send();
        Assert.assertTrue(r.isSuccessful());        
    }    
    
    @Then("^I expect the response to fail$")
    public void I_expect_the_response_to_fail() throws Throwable {
        Response r = request.send();
        Assert.assertFalse(r.isSuccessful());
    }
    
}
