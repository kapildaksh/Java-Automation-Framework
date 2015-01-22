/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.LinkedList;
import java.util.List;
import org.testng.Assert;

/**
 *
 * @author Brian Becker
 */
public abstract class ExpectedResponse {
    
    @JsonIgnore
    private final List<Patch> ignores = new LinkedList<Patch>();
    @JsonIgnore
    private final List<Patch> patches = new LinkedList<Patch>();
    
    /**
     * Return the Expected Response in text format.
     * 
     * @return expected response
     */
    public abstract String expected();
    
    /**
     * Return the Returned Response or real response, in text format.
     * 
     * @return returned response
     */
    public abstract String returned();
        
    /**
     * Travel to the given path using Jackson-style path notation.
     * For instance, you may path(0, 1, "Name") to traverse a 2D
     * array and index the "Name" element in the selected object.
     * 
     * @param path Integer and String array pointing to location.
     * @return 
     */
    public ExpectedPath path(Object... path) {
        return new ExpectedPath(JsonPointer.fromPath(path), ignores, patches);
    }
    
    /**
     * Travel to the given path using JSON-Pointer style path notation.
     * For instance, you may at("/0/1/Name") to index into a 2D array
     * and the "Name" element in the selected object.
     * 
     * @param path Json Pointer String
     * @return 
     */
    public ExpectedPath at(String path) {
        return new ExpectedPath(path, ignores, patches);
    }    

    /**
     * Clear out all of the changes which have been done to the expected
     * response. This is called only internally, after the verification
     * step is done.
     * 
     * @return this
     */
    private ExpectedResponse clear() {
        this.ignores.clear();
        this.patches.clear();
        return this;
    }
    
    /**
     * Verify this Expected Response. There is no return value, it acts as
     * an assertion if the Expected Response and the newly returned response
     * differ. Being able to use slightly variant expected responses without
     * having to manually input all the test cases is the purpose for the
     * "path" and "at" methods. You can traverse the ExpectedPath, and use
     * this interface to remove or change expectations. If successful, these
     * expected changes are erased and the expectations are ready for another
     * new use.
     */
    public void verify() {
        if(expected() == null)
            Assert.fail("Nothing expected! Failed to load response.");
        if(returned() == null)
            Assert.fail("Nothing returned from REST service.");
        
        try {
            String expect = expected();
            String real = returned();
            for(Patch p : ignores) {
                expect = p.apply(expect);
                real = p.apply(real);
            }
            for(Patch p : patches) {
                expect = p.apply(expect);
            }
            Assert.assertEquals(expect, real);
        } catch (Exception e) {
            Assert.fail("Exception occurred while sending message. (" + e.getMessage() + ")");
        } finally {
            this.clear();
        }
    }
    
}