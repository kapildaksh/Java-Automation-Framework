/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.rest;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The ExpectedResponse class is used for validating whether or not REST
 * requests return the proper values. It takes both a REST request as well
 * as an expected string.
 *
 * @author Brian Becker
 */
public interface ExpectedResponse {
    
    public abstract ExpectedPath expected();
    
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