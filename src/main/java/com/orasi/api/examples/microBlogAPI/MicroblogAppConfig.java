/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.api.examples.microBlogAPI;

import java.util.Arrays;

import javax.ws.rs.ext.RuntimeDelegate;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import java.util.Collection;
import javax.inject.Inject;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.DelegatingFilterProxy;

@Configuration
public class MicroblogAppConfig { 
    @Inject private MicroblogService blogService;
    
    @Autowired
    private ObjectPostProcessor<Object> opp;
    
    @Bean( destroyMethod = "shutdown" )
    public SpringBus cxf() {
        return new SpringBus();
    }
 
    @Bean
    public Server jaxRsServer() {
        JAXRSServerFactoryBean factory = RuntimeDelegate.getInstance().createEndpoint( jaxRsApiApplication(), JAXRSServerFactoryBean.class );        
        factory.setServiceBeans( Arrays.< Object >asList( microblogRestService() ) );
        factory.setAddress( "/rest" );
        factory.setProviders( Arrays.< Object >asList( json(), yaml(), new DetailedWebApplicationException() ) );
        return factory.create();
    }
    
    @Bean
    public Server jaxWsServer() {
        JaxWsServerFactoryBean factory = new JaxWsServerFactoryBean();
        factory.setServiceClass(MicroblogRestService.class);
        factory.setServiceBean( microblogRestService() );
        factory.setAddress( "/soap" );
        return factory.create();
    }    
    
    @Bean
    public UserDetailsService defaultUserDetailsService() {
        final MicroblogService svc = blogService;
        return new UserDetailsService() {

            @Override
            public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
                return new UserDetails() {

                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {
                        return Arrays.asList(new GrantedAuthority[] { new SimpleGrantedAuthority("user") });
                    }

                    @Override
                    public String getPassword() {
                        return svc.getUser(username).password;
                    }

                    @Override
                    public String getUsername() {
                        return username;
                    }

                    @Override
                    public boolean isAccountNonExpired() {
                        return true;
                    }

                    @Override
                    public boolean isAccountNonLocked() {
                        return true;
                    }

                    @Override
                    public boolean isCredentialsNonExpired() {
                        return true;
                    }

                    @Override
                    public boolean isEnabled() {
                        return true;
                    }
                    
                };
            }
            
        };
    }
    
    @Bean
    public AuthenticationManager defaultAuthenticationManager(UserDetailsService svc) throws Exception {
        return new AuthenticationManagerBuilder(opp).userDetailsService(svc).and().build();
    }
    
    @Bean
    public BasicAuthenticationEntryPoint authenticationEntryPoint() {
        BasicAuthenticationEntryPoint ep = new BasicAuthenticationEntryPoint();
        ep.setRealmName("orasi-microblog-example");
        return ep;
    }
    
    @Bean
    public BasicAuthenticationFilter basicAuthenticationFilter(BasicAuthenticationEntryPoint ep, AuthenticationManager am) {
        BasicAuthenticationFilter filter = new BasicAuthenticationFilter(am, ep);
        return filter;
    }
    
    @Bean
    public DelegatingFilterProxy springSecurityFilterChain(BasicAuthenticationFilter filter) throws Exception {
        DelegatingFilterProxy dfp = new DelegatingFilterProxy(filter);
        return dfp;
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
    public JacksonYamlProvider yaml() {
        return new JacksonYamlProvider();
    }
    
    @Bean
    public JacksonJaxbJsonProvider json() {
        return new JacksonJaxbJsonProvider();
    }
    
}
