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
    public List<Patch> ignores = new LinkedList<Patch>();   
    @JsonIgnore
    public List<Patch> patches = new LinkedList<Patch>();      
    
    public abstract String expected();
    public abstract String returned();
    
    public ExpectedResponse ignore(String path) {
        Patch p = new Patch.Builder().remove(path).build();
        this.ignores.add(p);
        return this;
    }

    public ExpectedResponse replace(String path, Object value) {
        Patch p = new Patch.Builder().replace(path, value).build();
        this.patches.add(p);
        return this;
    }

    public ExpectedResponse add(String path, Object value) {
        Patch p = new Patch.Builder().add(path, value).build();
        this.patches.add(p);
        return this;
    }

    public ExpectedResponse remove(String path) {
        Patch p = new Patch.Builder().remove(path).build();
        this.patches.add(p);
        return this;
    }

    public ExpectedResponse clear() {
        this.ignores.clear();
        this.patches.clear();
        return this;
    }
    
    public void verify() {
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
        }
    }
    
}