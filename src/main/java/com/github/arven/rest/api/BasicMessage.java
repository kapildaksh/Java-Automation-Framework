/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.rest.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * This message represents some basic CRUD operation has completed, or
 * possibly failed.
 * 
 * @author Brian Becker
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class BasicMessage {
    public static enum RequestType { CREATE, READ, UPDATE, DELETE, PROCESS };

    private int errorCode;
    public int getErrorCode() { return errorCode; }
    
    private RequestType type;
    public RequestType getType() { return type; }
    
    private String message;
    public String getMessage() { return message; }
    
    public boolean getSuccessful() { return this.errorCode == 200; }
    
    public BasicMessage() { }
    
    public BasicMessage(int errorCode, RequestType type, String message) {
        this.type = type;
        this.errorCode = errorCode;
        this.message = message;
    }
}
