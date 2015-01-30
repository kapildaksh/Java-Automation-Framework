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
     * Travel to the given path, from this node. Uses the Jackson-style path
     * selection, strings (for object) and integers (for arrays).
     * 
     * @param path  Target path, Integers or Strings only.
     * @return new node referring to the selected path
     */
    public ExpectedNode path(Object... path);
    
    /**
     * Travel to the given path, from this node. Uses the JSON Pointer style
     * path selection.
     * 
     * @param path  Target path, String specifying JSON Pointer
     * @return new node referring to the selected pointer
     */
    public ExpectedNode at(String path);
    
    /**
     * Verify this Expected Response. Real is returned, additionally, it acts as
     * an assertion if the Expected Response and the newly returned response
     * differ. Being able to use slightly variant expected responses without
     * having to manually input all the test cases is the purpose for the
     * expected method, which can be used to traverse the path of nodes and
     * change expected values.
     * 
     * @return a value which matches the modified expectations
     */
    public abstract JsonNode verify();
    
}