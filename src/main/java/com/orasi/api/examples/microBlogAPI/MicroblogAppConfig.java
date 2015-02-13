/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.api.examples.microBlogAPI;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.util.Arrays;

import javax.ws.rs.ext.RuntimeDelegate;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.xml.JacksonJaxbXMLProvider;

@Configuration
public class MicroblogAppConfig { 
    @Bean( destroyMethod = "shutdown" )
    public SpringBus cxf() {
        return new SpringBus();
    }
 
    @Bean
    public Server jaxRsServer() {
        JAXRSServerFactoryBean factory = RuntimeDelegate.getInstance().createEndpoint( jaxRsApiApplication(), JAXRSServerFactoryBean.class );        
        factory.setServiceBeans( Arrays.< Object >asList( microblogRestService() ) );
        factory.setAddress( "/" + factory.getAddress() );
        factory.setProviders( Arrays.< Object >asList( json(), xml(), yaml(), new DetailedWebApplicationException() ) );
        return factory.create();
    }
 
    @Bean 
    public MicroblogApplication jaxRsApiApplication() {
        return new MicroblogApplication();
    }
 
    @Bean 
    public MicroblogRestService microblogRestService() {
        return new MicroblogRestService();
    }
 
    @Bean 
    public MicroblogService peopleService() {
        return new MicroblogService();
    }
  
    @Bean
    public JacksonYamlProvider yaml() {
        return new JacksonYamlProvider();
    }
    
    @Bean
    public JacksonJaxbJsonProvider json() {
        return new JacksonJaxbJsonProvider();
    }
    
    @Bean
    public JacksonJaxbXMLProvider xml() {
        return new JacksonJaxbXMLProvider();
    }    
}
