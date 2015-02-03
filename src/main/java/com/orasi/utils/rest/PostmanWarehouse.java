/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.rest;

import java.util.Map;

/**
 * The PostmanWarehouse allows for the retrieval of a number of Postman
 * Collections as well as Postman Environments and other (potentially
 * in the future) JSON data stores from Postman.
 * 
 * @author Brian Becker
 */
public class PostmanWarehouse {
    
    private final String directory;
    
    public PostmanWarehouse(String directory) {
        this.directory = directory;
    }
    
    public static PostmanWarehouse dir(String directory) {
        return new PostmanWarehouse(directory);
    }
    
    public RestCollection collection(String name) {
        return PostmanCollection.file(getClass().getResource(directory + name + ".json.postman_collection"));
    }
    
    public Map environment(String name) {
        return PostmanEnvironment.file(getClass().getResource(directory + name + ".postman_environment"));
    }
}
