/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.sandbox;

import com.fasterxml.jackson.databind.JsonNode;
import com.orasi.utils.Constants;
import com.orasi.utils.dataProviders.JacksonDataProviderFactory;
import com.orasi.utils.types.IteratorMap;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.collections.iterators.ArrayIterator;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * These are some basic tests for the JSON Data Provider, in both node based
 * traversal and a class mapping. This allows passing very large and complex
 * structures into test cases for things such as web services.
 * 
 * NOTE: Where JSON itself is the structure that should be passed into a test
 * case, you should escape it and use any data provider you like. This is for
 * passing object-like data to test cases.
 * 
 * @author Brian Becker
 */
public class TestCSVDataProvider {
    
    public Path getFilePath(String name) throws Throwable {
        return Paths.get(getClass().getResource(Constants.BLUESOURCE_DATAPROVIDER_PATH).toURI()).resolve(name);
    }

    @DataProvider(name = "dataArrayCSV")
    public Iterator<Object[]> dataArrayCSV() throws Throwable {
        return JacksonDataProviderFactory.getJsonFactory(getFilePath("TestJSONArray.json")).createArrayParams().getData();
    }
    
    @DataProvider(name = "dataArrayNodeCSV")
    public Iterator<Object[]> dataArrayNodeCSV() throws Throwable {
        return JacksonDataProviderFactory.getJsonFactory(getFilePath("TestJSONArray.json")).createArrayNode().getData();
    }
    
    @DataProvider(name = "dataArrayArrayCSV")
    public Iterator<Object[]> dataArrayArrayCSV() throws Throwable {
        return JacksonDataProviderFactory.getJsonFactory(getFilePath("TestJSONArray.json")).createArrayStructured(int[].class).getData();
    }
    
    @Test(dataProvider = "dataArrayCSV")
    public void testArrayCSV(int a, int b, int c, int d, int e) {
        Assert.assertTrue(a == 1 || a == 5);
        Assert.assertTrue(b == 2 || b == 4);
        Assert.assertTrue(c == 3 || c == 3);
        Assert.assertTrue(d == 4 || d == 2);
        Assert.assertTrue(e == 5 || e == 1);
    }
    
    @Test(dataProvider = "dataArrayNodeCSV")
    public void testArrayNodeCSV(JsonNode a) {
        Iterator<Integer> it = new IteratorMap<JsonNode, Integer>(a.iterator()) {
            @Override
            public Integer apply(JsonNode i) {
                return i.asInt();
            }
        };
        
        if(it.next().equals(1)) {
            Assert.assertEquals(it, new ArrayIterator(new Integer[] { 2, 3, 4, 5 }));
        } else {
            Assert.assertEquals(it, new ArrayIterator(new Integer[] { 4, 3, 2, 1 }));
        }
    }
    
    @Test(dataProvider = "dataArrayArrayCSV")
    public void testArrayArrayCSV(int[] a) {
        Assert.assertTrue(java.util.Arrays.equals(a, new int[] {1, 2, 3, 4, 5}) || java.util.Arrays.equals(a, new int[] { 5, 4, 3, 2, 1 }));
    }
        
}
