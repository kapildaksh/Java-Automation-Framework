/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.api.examples.microBlogAPI;

import javax.servlet.FilterRegistration;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.servlet.ServletRegistration;
import org.glassfish.grizzly.servlet.WebappContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * Basic Servlet Container
 * 
 * @author Brian Becker
 */
public class MicroblogRunner {

    public static void main(String[] args) throws Exception{
        HttpServer server = new HttpServer();
        NetworkListener listener = new NetworkListener("grizzly2", "localhost", 4567);
        server.addListener(listener);
        
        WebappContext ctx = new WebappContext("ctx", "/v1");
        final ServletRegistration reg = ctx.addServlet("cxf", new CXFServlet());
        reg.addMapping("/*");
        ctx.setInitParameter( "contextClass", AnnotationConfigWebApplicationContext.class.getName() );
        ctx.setInitParameter( "contextConfigLocation", MicroblogAppConfig.class.getName() );
        ctx.addListener("org.springframework.web.context.ContextLoaderListener");
        ctx.addListener("org.springframework.web.context.request.RequestContextListener");
        ctx.addContextInitParameter( "contextClass" , AnnotationConfigWebApplicationContext.class.getName() );
        ctx.addContextInitParameter( "contextConfigLocation" , SecurityConfig.class.getName());
        FilterRegistration freg = ctx.addFilter("springSecurityFilterChain", "org.springframework.web.filter.DelegatingFilterProxy");
        freg.addMappingForUrlPatterns(null, true, "/*");
        
        ctx.deploy(server);
        
        server.start();
        System.in.read();
    }
    
}
