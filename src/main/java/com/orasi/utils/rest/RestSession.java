package com.orasi.utils.rest;

import java.net.CookieManager;
import java.net.CookiePolicy;

/**
 * The RestSession signifies a central repository for a given RestCollection
 * which can be used to store state-related data. Primarily, in the realm of
 * web services we will be dealing with cookies which hold a session key.
 * 
 * @author Brian Becker
 */
public class RestSession {
    private CookieManager cookieManager = new CookieManager();
    
    public RestSession() {
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
    }
    
    public CookieManager getCookieManager() {
        return this.cookieManager;
    }
}
