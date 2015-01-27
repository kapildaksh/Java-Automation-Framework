/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.rest;

import java.net.CookieManager;
import java.net.CookiePolicy;

/**
 *
 * @author brian.becker
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
