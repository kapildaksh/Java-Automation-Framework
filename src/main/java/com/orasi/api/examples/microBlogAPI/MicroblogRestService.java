/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.api.examples.microBlogAPI;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.cxf.interceptor.Fault;

/**
 * This is the Microblog Example implemented in a JAX-RS Service.
 * 
 * @author Brian Becker
 */
public class MicroblogRestService {
    
    @Inject private MicroblogService blogService;
    
    @GET @Path("/version") @Produces(MediaType.TEXT_PLAIN)
    public String version() {
        return "v1.0";
    }
    
    @POST @Path("/user") @Produces(MediaType.APPLICATION_JSON)
    public void addUser(User user) {
        blogService.addUser(user);
    }
    
    @GET @Path("/user/{name}") @Produces({ MediaType.APPLICATION_JSON })
    public User getUser(@PathParam("name") String name) throws Fault {
        System.out.println(blogService.getUser(name));
        return blogService.getUser(name);
    }
    
}
