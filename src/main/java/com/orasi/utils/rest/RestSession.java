package com.orasi.utils.rest;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Map;

/**
 * The RestSession signifies a central repository for a given RestCollection
 * which can be used to store state-related data. Primarily, in the realm of
 * web services we will be dealing with cookies which hold a session key.
 * 
 * @author  Brian Becker
 */
public class RestSession {
    
    private final CookieManager cookieManager = new CookieManager();
    private Map environment;
    
    /**
     * Create a new RestSession. This is a repository for a given RestCollection
     * of various types of information, such as cookie manager, global
     * environment variables, etc.
     */
    public RestSession() {
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
    }
    
    /**
     * Retrieve the CookieManager. This is used for transferring the state
     * of login between different RestCollection's.
     * 
     * @return CookieManager
     */
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
    
}
