package com.orasi.utils.rest;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Map;

/**
 * The RestSession signifies a central repository for a given RestCollection
 * which can be used to store state-related data. Primarily, in the realm of
 * web services we will be dealing with cookies which hold a session key.
 * 
 * @author Brian Becker
 */
public class RestSession {
    
    private CookieManager cookieManager = new CookieManager();
    private Map environment;
    
    public RestSession() {
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
    }
    
    public CookieManager getCookieManager() {
        return this.cookieManager;
    }
    
    /**
     * Pass an environment to the whole collection. Not only is this permanent,
     * not being cleared on requests, it also applies to all instances of the
     * request object. This can cause numerous debugging issues, but the
     * values from these replacements will always be overloaded by local
     * variables.
     * 
     * @param variables
     * @return 
     */    
    public RestSession env(Map variables) {
        this.environment = variables;
        return this;
    }
    
    public Map env() {
        return this.environment;
    }
    
}
