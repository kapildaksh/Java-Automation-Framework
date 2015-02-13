/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.api.examples.microBlogAPI;

import org.apache.cxf.transport.servlet.CXFServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.servlet.ServletRegistration;
import org.glassfish.grizzly.servlet.WebappContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

/**
 * Basic Servlet Container
 * 
 * @author Brian Becker
 */
public class MicroblogRunner {

    private static final String SERVLET_CONTEXT_PATH = "";

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
        ctx.deploy(server);
        
        server.start();
        System.in.read();
    }
    
}
