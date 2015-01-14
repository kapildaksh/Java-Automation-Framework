/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.rest.api;

/**
 *
 * @author brian.becker
 */
public class ServiceResponse {
    public int code;
    public Type type;
    
    public static enum Type {
        ERROR, SUCCESS
    }
    
    public ServiceResponse() {}
   
    public ServiceResponse(Type type, int code) {
        this.type = type;
        this.code = code;
    }
}
