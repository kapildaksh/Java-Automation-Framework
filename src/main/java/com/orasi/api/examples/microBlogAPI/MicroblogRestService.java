/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.api.examples.microBlogAPI;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.Consumes;
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
@WebService(targetNamespace = "http://examples.api.orasi.com/microblogAPI")
public class MicroblogRestService {
    
    @Inject private MicroblogService blogService;
    
    @RolesAllowed({"user"}) @WebMethod @GET @Path("/version") @Produces(MediaType.TEXT_PLAIN)
    public String version() {
        return "v1.0";
    }

    @PermitAll @WebMethod @POST @Path("/user") @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public void addUser(User user) {
        blogService.addUser(user);
    }
    
    @WebMethod @GET @Path("/user/{name}") @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public User getUser(@PathParam("name") String name) throws Fault {
        return blogService.getUser(name);
    }
    
    @WebMethod @POST @Path("/post") @Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public void postMessage(Post post) {
        blogService.addPost(post);
    }
    
    @WebMethod @GET @Path("/post/{name}") @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public void getMessagesByUser(@PathParam("name") String name) {
        blogService.getPosts(name);
    }
    
}
