/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.rest.misc;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.google.common.base.Function;
import com.orasi.arven.sandbox.MockMicroblogServer;
import com.orasi.utils.rest.ExpectedResponse;
import com.orasi.utils.rest.PostmanCollection;
import com.orasi.utils.rest.PostmanCollection.SampleResponse;
import com.orasi.utils.rest.RestCollection;
import com.squareup.okhttp.Response;
import java.util.Date;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * The MicroBlog example server is a simple Jetty service which is started up
 * by this test. It is a volatile test double which is used to simulate some
 * web service like Twitter. It does not hold any state, but various actions
 * are performed on the in-memory data with a "clean slate" on every startup.
 * 
 * @author Brian Becker
 */
public class MicroblogExampleTest {
    
    public static final String REST_SANDBOX = "/rest/sandbox/";
    
    public ObjectMapper map;
    public RestCollection collection;
    public MockMicroblogServer server;
    
    @BeforeClass
    public void setUp() throws Exception {
        map = new ObjectMapper();
        collection = PostmanCollection.file(getClass().getResource(REST_SANDBOX + "MicroBlog.json.postman_collection"));
        server = new MockMicroblogServer();
        server.start();
    }
    
    @AfterClass
    public void tearDown() {
        server.stop();
    }    
        
    @Test(groups = "usersExample")
    public void createUserTomExample() throws Exception {
        collection.byName("Create User Tom").response("createUserTomExample").verify();
    }
    
    @Test(groups = "usersExample")
    public void createUserLarryExample() throws Exception {
        collection.byName("Create User Larry").response("createUserLarryExample").verify();     
    }

    @Test(groups = "usersVerifyExample", dependsOnGroups = "usersExample")
    public void verifyUserTomExample() throws Exception {
        collection.byName("Check User Tom").response("verifyUserTomExample").verify();
    }    
    
    @Test(groups = "usersVerifyExample", dependsOnGroups = "usersExample")
    public void verifyUserLarryExample() throws Exception {
        collection.byName("Check User Larry").response("verifyUserLarryExample").verify();
    }
    
    @Test(groups = "postsExample", dependsOnGroups = "usersExample")
    public void createPostTomExample() throws Exception {
        collection.byName("Tom Posts Message").response("createPostTomExample").verify();
    }
    
    @Test(groups = "postsExample", dependsOnGroups = "usersExample")
    public void createPostLarryExample() throws Exception {
        collection.byName("Lots of Hash Tags").response("createPostLarryExample").verify();
    }
    
    @Test(groups = "postsVerifyExample", dependsOnGroups = "postsExample")
    public void verifyPostTomExample() throws Exception {
        ExpectedResponse res = collection.byName("Check Tom's Posts").response("verifyPostTomExample");
        res.ignore("/0/created");
        res.verify();
    }
    
    @Test(groups = "postsVerifyExample", dependsOnGroups = "postsExample")
    public void verifyPostLarryExample() throws Exception {
        ExpectedResponse res = collection.byName("Read Larry's Posts").response("verifyPostLarryExample");
        res.ignore("/0/created");
        res.verify();
    }
    
    @Test(groups = "postsVerifyExample", dependsOnGroups = "postsExample")
    public void verifyPostLarrySingleExample() throws Exception {
        ExpectedResponse res = collection.byName("Another Way to Read a Post").response("verifyPostLarrySingleExample");
        res.ignore("/created");
        res.verify();
    }
    
    @Test(groups = "friendsExample", dependsOnGroups = "usersVerifyExample")
    public void addFriendTomLarryExample() throws Exception {
        collection.byName("Tom Adds Larry").response("addFriendTomLarryExample").verify();
    }
    
    @Test(groups = "friendsExample", dependsOnGroups = "usersVerifyExample")
    public void addFriendLarryTomExample() throws Exception {
        collection.byName("Larry Adds Tom Back").response("addFriendLarryTomExample").verify();
    }
    
    @Test(groups = "friendsVerifyExample", dependsOnGroups = "friendsExample")
    public void verifyFriendTomLarryExample() throws Exception {
        collection.byName("Check User Tom").response("verifyFriendTomLarryExample").verify();
    }
    
    @Test(groups = "friendsVerifyExample", dependsOnGroups = "friendsExample")
    public void verifyFriendLarryTomExample() throws Exception {
        collection.byName("Check User Larry").response("verifyFriendLarryTomExample").verify();
    }
    
    @Test(groups = "friendsRemoveExample", dependsOnGroups = "friendsVerifyExample")
    public void removeFriendLarryTomExample() throws Exception {
        collection.byName("Larry Removes Tom").response("removeFriendLarryTomExample").verify();
    }
    
    @Test(groups = "friendsRemoveVerifyExample", dependsOnGroups = "friendsRemoveExample")
    public void verifyRemoveFriendLarryTomExample() throws Exception {
        collection.byName("Check User Larry").response("verifyRemoveFriendLarryTomExample").verify();
    }
    
    @Test(groups = "patchEmailExample", dependsOnGroups = "friendsRemoveVerifyExample")
    public void patchLarryEmailExample() throws Exception {
        collection.byName("Larry Adds Email Address").response("patchLarryEmailExample").verify();
    }
    
    @Test(groups = "patchEmailVerifyExample", dependsOnGroups = "patchEmailExample")
    public void verifyPatchEmailExample() throws Exception {
        collection.byName("Check User Larry").response("verifyPatchEmailExample").verify();
    }
    
}
