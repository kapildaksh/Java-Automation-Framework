package com.orasi.rest.misc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orasi.api.demos.MockMicroblogServer;
import com.orasi.utils.rest.BaseExpectedNode;
import com.orasi.utils.rest.Json;
import com.orasi.utils.rest.OkRestRequest;
import com.orasi.utils.rest.PostmanWarehouse;
import com.orasi.utils.rest.RestCollection;
import com.orasi.utils.rest.RestRequest;
import com.orasi.utils.rest.RestResponse;
import com.orasi.utils.types.DefaultingMap;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
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
public class MicroblogSteps {
    
    public static final String REST_SANDBOX = "/com/orasi/rest/misc/";
    public static final MockMicroblogServer server = new MockMicroblogServer();
    
    public ObjectMapper map;
    public RestCollection collection;
    public Map env1;
    public Map env2;
    
    public Map nvars = new HashMap();
    
    private RestRequest request = null;
    private BaseExpectedNode node = new BaseExpectedNode();
    
    private final List<String> ignores = new ArrayList<String>();
    
    static {
        server.start();
    }
    
    public MicroblogSteps() throws Exception {
        PostmanWarehouse wh = PostmanWarehouse.dir(REST_SANDBOX);
        collection = wh.collection("MicroBlog");
        env1 = wh.environment("Passwords");
        collection.session().env(env1);
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
        collection.get("Logout").send();
    }
    
    @Given("^I am logged in as (.*)$")
    public void logged_in(String user) throws Throwable {
        RestResponse res = collection.get("Login As " + user).send();
        Assert.assertEquals(res.code(), 200);                
    }
    
    @When("^I log in as \"(.*)\" with password \"(.*)\"")
    public void log_in(String user, String password) throws Throwable {
        Map m = new HashMap();
        m.put("username", user);
        m.put("password", password);
        collection.get("Login As Variable").env(m).send();
    }
    
    @Given("(.*)'s account has been created$")
    public void account_created(String label) throws Throwable {
        collection.get("Create User " + label).send();
    }
    
    @Given("^the (.*) group has been created by (.*)$")
    public void group_created(String label, String user) throws Throwable {
        collection.get("Create User " + user).send();
        collection.get("Login As " + user).send();
        collection.get("Create Group " + label).send();
        collection.get("Logout").send();
    }
    
    @Given("^(.*) has joined group (.*)")
    public void group_joined(String user, String label) throws Throwable {
        collection.get("Create User " + user).send();
        collection.get("Login As " + user).send();
        collection.get("Join Group " + label).send();
        collection.get("Logout").send();
    }
    
    @Given("^the following groups:$")
    public void groups_created(DataTable table) throws Throwable {
        Map<String, String> m = table.asMap(String.class, String.class);
        for(Map.Entry<String, String> e : m.entrySet()) {
            String[] vals = e.getValue().split(" ");
            group_created(e.getKey(), vals[0]);
            for(int i = 1; i < vals.length; i++) {
                group_joined(vals[i], e.getKey());
            }
        }
    }
    
    @Given("(.*) and (.*) are mutually exclusive$")
    public void mutually_exclusive(String first, String second) throws Throwable {
        Map m = new HashMap();
        m.put("first", first.toLowerCase());
        m.put("second", second.toLowerCase());
        RestResponse res = collection.get("Set Mutual Exclusion").env(m).send();
        Assert.assertEquals(res.code(), 200);        
    }
    
    @Given("(.*) and (.*) are not mutually exclusive$")
    public void not_mutually_exclusive(String first, String second) throws Throwable {
        Map m = new HashMap();
        m.put("first", first.toLowerCase());
        m.put("second", second.toLowerCase());
        RestResponse res = collection.get("Unset Mutual Exclusion").env(m).send();
        Assert.assertEquals(res.code(), 200);
    }    
    
    @And("^(.*) has (.*) on (.*) list of friends$")
    public void friends_list(String first, String second, String pronoun) throws Throwable {
        collection.get("Create User " + first).send();
        collection.get("Create User " + second).send();
        collection.get("Login As " + first).send();        
        collection.get(first + " Adds " + second).send();
        collection.get("Logout").send();        
    }    

    @And("^(.*)'s account has not been created$")
    public void account_not_created(String label) {
    }
    
    @When("^I send a request to (.*)$")
    public void request(String label) throws Throwable {
        request = collection.get(label).env(env2);
    }
    
    @And("^I define (.*):$")
    public void variables(String variable, DataTable table) throws Throwable {
        nvars.put(variable, table);
        switch (variable) {
            case "the environment":
                collection.session().env(table.asMap(String.class, String.class));
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
        request.response(response, node).verify();
        node = new BaseExpectedNode();
    }
    
    @Then("^I expect a response matching:$")
    public void matches_doc(String response) throws Throwable {
        RestResponse res = request.env(env2).send();
        node.verify(res.json(), Json.Map.readTree(response));
    }    
    
    @Then("^I expect a response with code (\\d+) .*$")
    public void error_code(String code) throws Throwable {
        RestResponse res = request.env(env2).send();
        Assert.assertEquals(String.valueOf(res.code()), code, res.data());
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
    
    @When("^I send the (.*) request to (.*):$")
    public void request_document(String method, String url, String req) throws Exception {
        Map defVar = new DefaultingMap(env1, env2);
        RequestBody body = RequestBody.create(null, req);
        Request okreq = new Request.Builder().url(url).method(method, body).build();
        request = new OkRestRequest(okreq).session(collection.session()).env(defVar);
    }
    
}
