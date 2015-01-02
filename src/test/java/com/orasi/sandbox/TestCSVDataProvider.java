/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.sandbox;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.orasi.utils.Constants;
import com.orasi.utils.dataProviders.JacksonDataProviderFactory;
import com.orasi.utils.types.IteratorMap;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import org.apache.commons.collections.iterators.ArrayIterator;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * These are some basic tests for the CSV Data Provider, in both node based
 * traversal and a class mapping. This allows passing very large and complex
 * structures into test cases for things such as web services.
 * 
 * NOTE: CSV is a popular choice for tabular data which doesn't have a lot of
 * structure. It's an effective way to specify method parameters.
 * 
 * @author Brian Becker
 */
public class TestCSVDataProvider {
    
    public Path getFilePath(String name) throws Throwable {
        return Paths.get(getClass().getResource(Constants.BLUESOURCE_DATAPROVIDER_PATH).toURI()).resolve(name);
    }

    @DataProvider(name = "dataActuallyValidCSV")
    public Iterator<Object[]> dataActuallyValidCSV() throws Throwable {
        return JacksonDataProviderFactory.getCsvFactory(getFilePath("TestCSVActuallyValid.csv")).createArrayParams().getData();
    }
    
    @DataProvider(name = "dataArrayCSV")
    public Iterator<Object[]> dataArrayCSV() throws Throwable {
        return JacksonDataProviderFactory.getCsvFactory(getFilePath("TestCSVArray.csv")).createArrayParams().getData();
    }
    
    @DataProvider(name = "dataArrayNodeCSV")
    public Iterator<Object[]> dataArrayNodeCSV() throws Throwable {
        return JacksonDataProviderFactory.getCsvFactory(getFilePath("TestCSVArray.csv")).createArrayNode().getData();
    }
    
    @DataProvider(name = "dataArrayArrayCSV")
    public Iterator<Object[]> dataArrayArrayCSV() throws Throwable {
        return JacksonDataProviderFactory.getCsvFactory(getFilePath("TestCSVArray.csv")).createArrayStructured(int[].class).getData();
    }
    
    @Test(dataProvider = "dataArrayCSV")
    public void testArrayCSV(String a, String b, String c, String d, String e) {
        Assert.assertTrue("1".equals(a) || "5".equals(a));
        Assert.assertTrue("2".equals(b) || "4".equals(b));
        Assert.assertTrue("3".equals(c) || "3".equals(c));
        Assert.assertTrue("4".equals(d) || "2".equals(d));
        Assert.assertTrue("5".equals(e) || "1".equals(e));
    }
    
    @Test(dataProvider = "dataActuallyValidCSV")
    public void testActuallyValidCSV(String a, String b, String c, String d, String e) throws Throwable {
        switch (e) {
            case "3":
                Assert.assertEquals(a, "Testing, Testing, 1, 2, 3");
                break;
            case "4":
                Assert.assertEquals(e, "This\nis actually valid\n");
            case "2":
                Assert.assertEquals(c, "We can have \"escaped\" quotes");
                Assert.assertNotEquals(a, "Testing, Testing, 1, 2, 3");
                break;
        }
    }
    
    @Test(dataProvider = "dataArrayNodeCSV")
    public void testArrayNodeCSV(ArrayNode a) {
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
