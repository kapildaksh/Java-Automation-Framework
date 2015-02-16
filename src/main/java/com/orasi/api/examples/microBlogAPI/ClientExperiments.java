/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.api.examples.microBlogAPI;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.orasi.utils.rest.Yaml;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.MediaType;
import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;

/**
 *
 * @author brian.becker
 */
public class ClientExperiments {
    
    public static void main(String args[]) throws Exception {
        MicroblogRunner.main(args);
        
        MicroblogRestService svc = JAXRSClientFactory.create("http://localhost:4567/v1/rest/", MicroblogRestService.class);
        WebClient.client( svc ).accept( MediaType.APPLICATION_XML_TYPE );
        User u = new User();
        u.username = "trfields";
        u.nickname = "Tom";
        u.email    = "tom@tom.name";
        u.password = "xyzzy";
        svc.addUser(u);
        System.out.println(Yaml.Map.writerWithDefaultPrettyPrinter().writeValueAsString(svc.getUser("trfields")));
    }
    
}
