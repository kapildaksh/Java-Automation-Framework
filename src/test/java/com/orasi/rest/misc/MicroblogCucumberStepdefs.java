/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.rest.misc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orasi.arven.sandbox.rest.MockMicroblogServer;
import com.orasi.utils.rest.BaseExpectedNode;
import com.orasi.utils.rest.ExpectedResponse;
import com.orasi.utils.rest.PostmanCollection;
import com.orasi.utils.rest.PostmanEnvironment;
import com.orasi.utils.rest.RestCollection;
import com.orasi.utils.rest.RestRequest;
import com.orasi.utils.rest.RestSession;
import com.squareup.okhttp.Response;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.And;
import cucumber.api.java.en.When;
import cucumber.api.java.en.Then;
import cucumber.api.java.Before;
import cucumber.api.java.After;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.testng.Assert;

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
    
    public Map nvars = new HashMap();
    
    private RestRequest request = null;
    private ExpectedResponse expected = null;
    private BaseExpectedNode node = null;
    
    private final List<String> ignores = new ArrayList<String>();
    
    static {
        server.start();
    }
    
    public MicroblogCucumberStepdefs() throws Exception {
        collection = PostmanCollection.file(getClass().getResource(REST_SANDBOX + "MicroBlog.json.postman_collection"));
        env1 = PostmanEnvironment.file(getClass().getResource(REST_SANDBOX + "Passwords.postman_environment"));
        collection.withEnv(env1);
        collection.withSession(new RestSession());
    }
    
    @Before
    public void beforeScenario() {
        server.clear();
    }

    @After
    public void afterScenario() {
    }
    
    @Given("^I am not logged in$")
    public void not_logged_in() throws Throwable {
        collection.byName("Logout").send();
    }
    
    @Given("^I am logged in as (.*)$")
    public void logged_in(String user) throws Throwable {
        collection.byName("Login As " + user).send();
    }
    
    @When("^I log in as \"(.*)\" with password \"(.*)\"")
    public void log_in(String user, String password) throws Throwable {
        Map m = new HashMap();
        m.put("username", user);
        m.put("password", password);
        collection.byName("Login As Variable").withEnv(m).send();
    }
    
    @Given("(.*)'s account has been created$")
    public void account_created(String label) throws Throwable {
        collection.byName("Create User " + label).send();
    }
    
    @And("^(.*) has (.*) on (.*) list of friends$")
    public void friends_list(String first, String second, String pronoun) throws Throwable {
        collection.byName("Create User " + first).send();
        collection.byName("Create User " + second).send();
        collection.byName("Login As " + first).send();        
        collection.byName(first + " Adds " + second).send();
        collection.byName("Logout").send();        
    }    

    @And("^(.*)'s account has not been created$")
    public void account_not_created(String label) {
    }
    
    @When("^I send a request to (.*)$")
    public void request(String label) throws Throwable {
        request = collection.byName(label).withEnv(env2);
    }
    
    @And("^I define (.*):$")
    public void variables(String variable, DataTable table) throws Throwable {
        nvars.put(variable, table);
        switch (variable) {
            case "the environment":
                collection.withEnv(table.asMap(String.class, String.class));
                break;
            case "replacements":
                env2 = table.asMap(String.class, String.class);
                break;
        }
    }
    
    @And("^I replace the values in (.*) with (.*)")
    public void substitution(String prefix, String var) {
        replace_all(prefix, (DataTable) nvars.get(var));
    }
    
    @Then("^I expect a response matching (.*)$")
    public void matches(String response) throws Throwable {
        if(node != null) {
            request.response(response, node).validate();
            node = null;
        } else {
            request.response(response).validate();
        }
    }
    
    @Then("^I expect a successful response")
    public void success() throws Throwable {
        Response res = request.send();
        Assert.assertTrue(res.isSuccessful());
    }
    
    @Then("^I expect a response with code (\\d+) .*$")
    public void error_code(String code) throws Throwable {
        Response res = request.send();
        Assert.assertEquals(String.valueOf(res.code()), code);
    }    

    @And("^I replace (.*) by (.*)$")
    public void replace(String with, String replace) {
        if(node == null) {
            node = new BaseExpectedNode();
        }
        node.at(with).replace(replace);
    }
    
    @And("^I replace (.*) by:$")
    public void replace_all(String prefix, DataTable table) {
        if(node == null) {
            node = new BaseExpectedNode();
        }
        for(Entry<String, String> e : table.asMap(String.class, String.class).entrySet()) {
            node.at(prefix + e.getKey()).replace(e.getValue());
        }
    }
    
    @And("^I ignore (.*)$")
    public void ignore(String with) {
        if(node == null) {
            node = new BaseExpectedNode();
        }
        for(String s : with.split(" ")) {
            node.at(s.trim()).ignore();
        }
    }
    
}
