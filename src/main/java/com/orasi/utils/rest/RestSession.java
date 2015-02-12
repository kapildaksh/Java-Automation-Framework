package com.orasi.utils.rest;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.ClientResponseContext;
import javax.ws.rs.client.ClientResponseFilter;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;

/**
 * The RestSession signifies a central repository for a given RestCollection
 * which can be used to store state-related data. Primarily, in the realm of
 * web services we will be dealing with cookies which hold a session key.
 * 
 * @author  Brian Becker
 */
public class RestSession implements ClientRequestFilter, ClientResponseFilter {
    
    private Map environment;
    private final Map<String, NewCookie> cookies;
    
    /**
     * Create a new RestSession. This is a repository for a given RestCollection
     * of various types of information, such as cookie manager, global
     * environment variables, etc.
     */
    public RestSession() {
        cookies = new HashMap<String, NewCookie>();
    }
    
    /**
     * Pass an environment to the whole collection. Not only is this permanent,
     * not being cleared on requests, it also applies to all instances of the
     * request object. This can cause numerous debugging issues, but the
     * values from these replacements will always be overloaded by local
     * variables.
     * 
     * @param   variables       Environment to be used as a global
     * @return  this
     */    
    public RestSession env(Map variables) {
        this.environment = variables;
        return this;
    }
    
    /**
     * This is the environment of the whole collection. It is stored in a
     * session-wide variable so that it's assignable to every single item
     * in the collection.
     * 
     * @return  Environment which is being used as a global
     */
    public Map env() {
        return this.environment;
    }

    @Override
    public void filter(ClientRequestContext requestContext) throws IOException {
        // System.out.println(requestContext.getHeaders());
        for(Cookie c : cookies.values()) {
            requestContext.getHeaders().add("Cookie", c.toString());
        }
    }

    @Override
    public void filter(ClientRequestContext arg0, ClientResponseContext arg1) throws IOException {
        cookies.putAll(arg1.getCookies());
    }
    
}
