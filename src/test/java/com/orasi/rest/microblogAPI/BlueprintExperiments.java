/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.rest.microblogAPI;

import com.orasi.api.demos.MockMicroblogServer;
import com.orasi.utils.rest.RestCollection;
import com.orasi.utils.rest.RestRequest;
import com.orasi.utils.rest.blueprint.SnowcrashCollection;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 *
 * @author brian.becker
 */
public class BlueprintExperiments {
    public static void main(String[] args) throws IOException, URISyntaxException, Exception {
        MockMicroblogServer server = new MockMicroblogServer();
        server.start();
        RestCollection c = SnowcrashCollection.file(BlueprintExperiments.class.getResource("/com/orasi/rest/microblogAPI/MicroBlog.md"), "http://localhost:4567");
        RestRequest r = c.get("Version: Check Version");
        System.out.println(r.response("200").verify().asText());
        System.out.println(c.get("Users: Add User: Humphrey").response("200").verify());
        server.stop();
    }
}
