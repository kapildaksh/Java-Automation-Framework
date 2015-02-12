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
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Brian Becker
 */
public class BlueprintExperiments {
    public static void main(String[] args) {
        MockMicroblogServer server = new MockMicroblogServer();
        try {
            server.start();
            RestCollection c = SnowcrashCollection.file(BlueprintExperiments.class.getResource("/com/orasi/rest/microblogAPI/MicroBlog.md"), "http://localhost:4567");
            RestRequest r = c.get("Version: Check Version");
            System.out.println(r.response("200").verify().asText());
            System.out.println(c.get("Users: Add User: Humphrey").response("200").verify());
            Map m = new HashMap();
            m.put("username", "way");
            m.put("nickname", "Wayfarer");
            m.put("password", "ajourneyman");
            Map p = new HashMap();
            p.put("user", "way");
            System.out.println(c.get("Users: Add User: Katherine").response("200").verify());
            System.out.println(c.get("Users: Add User: Variable").env(m).response("200").verify());
            System.out.println(c.get("Login: Authenticate: Katherine").response("200").verify());
            System.out.println(c.get("User: Check").env(m).params(p).response("200").verify());
        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {
            server.stop();
        }
    }
}
