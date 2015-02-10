/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.rest;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * This is a validation engine for the responses. Each and every collection
 * type requires the use of a validation class which will take the actual
 * rest request, the sample response, and a list of changes to be applied
 * to both of them programmatically. Then the request is fired off by the
 * validate, and a valid node is returned or an exception is thrown.
 */
public class ResponseVerifier implements ExpectedResponse {

    private final RestRequest request;
    private final String data;
    private final BaseExpectedNode node;

    /**
     * Create a new Response Validator with a REST request, data for
     * this class, as well as the patch node.
     * 
     * @param request
     * @param data
     * @param node 
     */
    public ResponseVerifier(RestRequest request, String data, BaseExpectedNode node) {
        this.request = request;
        this.data = data;
        this.node = node;
    }

    /**
     * Fire off the request and validate it. Both values (real and
     * expected) are patched, the equality of the expected and real
     * values are asserted, and a tree is returned if they are a
     * match.
     * 
     * @return 
     */
    @Override
    public JsonNode verify() {
        try {
            return this.node.verify(request.send().data().trim(), data.trim());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Select a path from which to start making changes to this validator.
     * The changes will be stored as a separate Patch which will be applied
     * to the expected node.
     * 
     * @param path
     * @return 
     */
    @Override
    public ExpectedNode path(Object... path) {
        return node.path(path);
    }

    /**
     * An alternate to using the .path("object", "path") style path
     * selection, which is a little less verbose for very long paths
     * and is easier to manipulate via string manipulations.
     * 
     * @param path
     * @return 
     */
    @Override
    public ExpectedNode at(String path) {
        return node.at(path);
    }

}
