/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.rest;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Function;
import com.squareup.okhttp.Response;
import java.util.LinkedList;
import java.util.List;
import org.testng.Assert;

/**
 * The ExpectedResponse class is used for validating whether or not REST
 * requests return the proper values. It takes both a REST request as well
 * as an expected string.
 *
 * @author Brian Becker
 */
public class ExpectedResponse {
    
    private final List<Patch> ignores = new LinkedList<Patch>();
    private final List<Patch> patches = new LinkedList<Patch>();
    private final RestRequest request;
    private String expected;
    private RestValidator validate;
    
    public ExpectedResponse(RestRequest req, String expected, RestValidator validate) {
        this.request = req;
        this.expected = expected;
        this.validate = validate;
    }

    public ExpectedResponse() {
        this(null, null, null);
    }
    
    /**
     * Get the expected path node, which can be used for changing the
     * expectations which the validator will have.
     * 
     * @return Expected Path node
     */
    public ExpectedPath expected() {
        return new ExpectedPath(null, ignores, patches);
    }
    
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
    public JsonNode validate() {
        if(request == null || expected == null || validate == null)
            throw new UnsupportedOperationException("Operation not supported on null ExpectedResponse");
        try {
            Response res = request.send();
            String real = res.body().string();
            for(Patch p : ignores) {
                expected = p.apply(expected);
                real = p.apply(real);
            }
            for(Patch p : patches) {
                expected = p.apply(expected);
            }
            Assert.assertEquals(expected, real);
            this.validate.validate(res);
            return Json.MAP.readTree(real);
        } catch (Exception e) {
            throw new RuntimeException("Error while sending message during validation. Validation failed.");
        }
    }
    
}