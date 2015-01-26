/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.rest.misc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orasi.arven.sandbox.rest.MockMicroblogServer;
import com.orasi.utils.rest.ExpectedResponse;
import com.orasi.utils.rest.PostmanCollection;
import com.orasi.utils.rest.PostmanEnvironment;
import com.orasi.utils.rest.RestCollection;
import com.orasi.utils.rest.RestRequest;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.But;
import cucumber.api.java.Before;
import cucumber.api.java.After;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * These are all of the step definitions that are used in the Cucumber
 * tests. To some extent they are reusable, because we are working with
 * a sort of data warehouse. However, for maximum effectiveness with
 * this framework, many custom steps should be defined. 
 *
 * @author Brian Becker
 */
public class MicroblogCucumberStepdefs {
    
    public static final String REST_SANDBOX = "/rest/sandbox/";
    public static final MockMicroblogServer server = new MockMicroblogServer();
    
    public ObjectMapper map;
    public RestCollection collection;
    public Map env1;
    public Map env2;
    
    private RestRequest request = null;
    private ExpectedResponse expected = null;
    
    static {
        server.start();
    }
    
    public MicroblogCucumberStepdefs() throws Exception {
        collection = PostmanCollection.file(getClass().getResource(REST_SANDBOX + "MicroBlog.json.postman_collection"));
        env1 = PostmanEnvironment.file(getClass().getResource(REST_SANDBOX + "Passwords.postman_environment"));
        env2 = new HashMap();
        env2.put("username", "arven2");
        env2.put("nickname", "A. R. Variadic");
        env2.put("email", "arvariadic@arven.info");        
        collection.withEnv(env1);
    }
    
    @Before
    public void beforeScenario() {
        server.clear();
    }

    @After
    public void afterScenario() {
    }
    
    @Given("^I am not logged in$")
    public void I_am_not_logged_in() {
        // Do nothing
    }
    
    @Given("^I am logged in as (.*)$")
    public void I_am_logged_in_as(String user) {
        // Do nothing
    }
    
    @Given("(.*)'s account has been created$")
    public void account_has_been_created(String label) throws Throwable {
        collection.byName("Create User " + label).send();
    }
    
    @And("^(.*) has (.*) on (.*) list of friends$")
    public void has_on_his_list_of_friends(String first, String second, String pronoun) throws Throwable {
        collection.byName(first + " Adds " + second).send();
    }    

    @And("^(.*)'s account has not been created$")
    public void account_has_not_been_created(String label) {
    }
    
    @When("^I send a request to (.*)$")
    public void I_send_a_request_to(String label) throws Throwable {
        request = collection.byName(label);
    }
    
    @And("^I set the variables to:$")
    public void I_set_the_variables_to(DataTable table) throws Throwable {
        request.withEnv(table.asMap(String.class, String.class));
    }
    
    @And("^I set the environment to:$")
    public void I_set_the_environment_to(DataTable table) throws Throwable {
        collection.withEnv(table.asMap(String.class, String.class));       
    }    
    
    @Then("^I expect a response matching (.*)$")
    public void I_expect_a_response_matching(String response) throws Throwable {
        expected = request.response(response);
        expected.validate();
    }
    
    @Then("^I want a response like (.*)$")
    public void I_want_a_response(String response) throws Throwable {
        expected = request.response(response);
    }

    @But("^with (.*) replaced by (.*)$")
    public void with_replaced_by(String with, String replace) {
        expected.at(with).replace(replace);
    }

    @But("^with (.*) ignored$")
    public void with_ignored(String with) {
        for(String s : with.split(" ")) {
            expected.at(s.trim()).ignore();
        }
    }

    @And("^I expect it to match the new values$")
    public void I_expect_it_to_match_the_new_values() {
        expected.validate();
    }
    
    @Then("^I expect the response to be valid$")
    public void I_expect_the_response_to_be_valid() throws Throwable {
        expected.validate();
    }
    
    @Then("^I expect the response to succeed$")
    public void I_expect_the_response_to_succeed() throws Throwable {
        request.send();
    }
    
}
