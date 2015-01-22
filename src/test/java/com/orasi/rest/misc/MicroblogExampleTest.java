/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.rest.misc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orasi.arven.sandbox.MockMicroblogServer;
import com.orasi.utils.rest.ExpectedResponse;
import com.orasi.utils.rest.PostmanCollection;
import com.orasi.utils.rest.PostmanEnvironment;
import com.orasi.utils.rest.RestCollection;
import java.util.HashMap;
import java.util.Map;
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
    public Map env1;
    public Map env2;
    public MockMicroblogServer server;
    
    @BeforeClass
    public void setUp() throws Exception {
        map = new ObjectMapper();
        collection = PostmanCollection.file(getClass().getResource(REST_SANDBOX + "MicroBlog.json.postman_collection"));
        env1 = PostmanEnvironment.file(getClass().getResource(REST_SANDBOX + "Passwords.postman_environment"));
        //new HashMap();
        //env1.put("username", "arven");
        //env1.put("nickname", "A. R. Varian");
        //env1.put("email", "arvarian@arven.info");
        env2 = new HashMap();
        env2.put("username", "arven2");
        env2.put("nickname", "A. R. Variadic");
        env2.put("email", "arvariadic@arven.info");        
        collection.env(env1);
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
    
    @Test(groups = "usersVariableExample")
    public void createUserVariableExample1() throws Exception {
        collection.byName("Create User Variable").response("createUserVariableExample").verify();
    }    
    
    @Test(groups = "usersVariableVerifyExample", dependsOnGroups = "usersVariableExample")
    public void verifyUserVariableExample1() throws Exception {
        ExpectedResponse res = collection.byName("Check User Variable").response("verifyUserVariableExample");
        res.verify();
    }
    
    @Test(groups = "usersVariableExample")
    public void createUserVariableExample2() throws Exception {
        collection.byName("Create User Variable").env(env2).response("createUserVariableExample").verify();
    }    
    
    @Test(groups = "usersVariableVerifyExample", dependsOnGroups = "usersVariableExample")
    public void verifyUserVariableExample2() throws Exception {
        ExpectedResponse res = collection.byName("Check User Variable").env(env2).response("verifyUserVariableExample");
        res.path("username").replace("arven2");
        res.path("nickname").replace("A. R. Variadic");
        res.path("email").replace("arvariadic@arven.info");
        res.verify();
    }    
    
    @Test(groups = "postsExample", dependsOnGroups = "usersExample")
    public void createPostTomExample() throws Exception {
        collection.byName("Tom Posts Message").response("createPostTomExample").verify();
    }
    
    @Test(groups = "postsExample", dependsOnGroups = "usersExample")
    public void createPostLarryExample() throws Exception {
        collection.byName("Lots of Hash Tags").response("createPostLarryExample").verify();
    }
    
    @Test(groups = "postsVerifyExample1", dependsOnGroups = "postsExample")
    public void verifyPostTomExample1() throws Exception {
        ExpectedResponse res = collection.byName("Check Tom's Posts").response("verifyPostTomExample");
        res.at("/0/created").ignore();
        res.verify();
    }
    
    @Test(groups = "postsVerifyExample1", dependsOnGroups = "postsExample")
    public void verifyPostLarryExample1() throws Exception {
        ExpectedResponse res = collection.byName("Read Larry's Posts").response("verifyPostLarryExample");
        res.at("/0/created").ignore();
        res.verify();
    }
    
    @Test(groups = "postsVerifyExample1", dependsOnGroups = "postsExample")
    public void verifyPostLarrySingleExample1() throws Exception {
        ExpectedResponse res = collection.byName("Another Way to Read a Post").response("verifyPostLarrySingleExample");
        res.at("/created").ignore();
        res.verify();
    }
    
    @Test(groups = "postsVerifyExample2", dependsOnGroups = "postsExample")
    public void verifyPostTomExample2() throws Exception {
        ExpectedResponse res = collection.byName("Check Tom's Posts").response("verifyPostTomExample");
        res.path(0, "created").ignore();
        res.verify();
    }
    
    @Test(groups = "postsVerifyExample2", dependsOnGroups = "postsExample")
    public void verifyPostLarryExample2() throws Exception {
        ExpectedResponse res = collection.byName("Read Larry's Posts").response("verifyPostLarryExample");
        res.path(0, "created").ignore();
        res.verify();
    }
    
    @Test(groups = "postsVerifyExample2", dependsOnGroups = "postsExample")
    public void verifyPostLarrySingleExample2() throws Exception {
        ExpectedResponse res = collection.byName("Another Way to Read a Post").response("verifyPostLarrySingleExample");
        res.path("created").ignore();
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
