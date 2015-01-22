/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.rest;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The ExpectedResponse class is used for validating whether or not REST
 * requests return the proper values. It provides an expected path API
 * as well as a way to initiate the associated request and validate the
 * expected response.
 *
 * @author Brian Becker
 */
public interface ExpectedResponse {
    
    /**
     * Edit this Expected Response. It is a node-based API very similar to
     * that of Jackson, however there are far fewer features. You can do
     * pretty much anything you could with JSON patching, as well as also
     * marking certain nodes which should not be considered at all.
     * 
     * @return An ExpectedPath for editing the ExpectedResponse
     */
    public abstract ExpectedPath edit();
    
    /**
     * Verify this Expected Response. Real is returned, additionally, it acts as
     * an assertion if the Expected Response and the newly returned response
     * differ. Being able to use slightly variant expected responses without
     * having to manually input all the test cases is the purpose for the
     * expected method, which can be used to traverse the path of nodes and
     * change expected values.
     * 
     * @return A validated JsonNode
     */
    public abstract JsonNode validate();
    
}