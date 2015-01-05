/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.sandbox;

import com.fasterxml.jackson.databind.JsonNode;
import com.orasi.utils.Constants;
import com.orasi.utils.dataProviders.DataProviders;
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
 * These are some basic tests for the YAML Data Provider, in both node based
 * traversal and a class mapping. This allows passing very large and complex
 * structures into test cases for things such as web services.
 * 
 * NOTE: Where YAML itself is the structure that should be passed into a test
 * case, you should escape it and use any data provider you like. This is for
 * passing object-like data to test cases.
 * 
 * @author Brian Becker
 */
public class TestYAMLDataProvider {
    
    private static final String[] DINING_LIST = { "napa-rose", "goofys-kitchen" };
    private static final int PARTY_SIZE = 1;
    
    public static class DiningTest {
        public List<String> diningList;
        public class DiningInfo {
            public Date date;
            public String time;
            public int partySize;
        }
        public DiningInfo diningInfo;
    }
    
    public Path getFilePath(String name) throws Throwable {
        return Paths.get(getClass().getResource(Constants.BLUESOURCE_DATAPROVIDER_PATH).toURI()).resolve(name);
    }

    @DataProvider(name = "dataDiningNodeYAML")
    public Iterator<Object[]> dataDiningNodeYAML() throws Throwable {
        return DataProviders.createYamlFactory().createHashNode(getFilePath("TestYAMLDining.yaml")).getData();
    }
    
    @DataProvider(name = "dataDiningClassYAML")
    public Iterator<Object[]> dataDiningClassYAML() throws Throwable {
        return DataProviders.createYamlFactory().createHashStructured(getFilePath("TestYAMLDining.yaml"), DiningTest.class).getData();
    }
    
    @DataProvider(name = "dataArrayYAML")
    public Iterator<Object[]> dataArrayYAML() throws Throwable {
        return DataProviders.createYamlFactory().createArrayParams(getFilePath("TestYAMLArray.yaml")).getData();
    }
    
    @DataProvider(name = "dataArrayNodeYAML")
    public Iterator<Object[]> dataArrayNodeYAML() throws Throwable {
        return DataProviders.createYamlFactory().createArrayNode(getFilePath("TestYAMLArray.yaml")).getData();
    }
    
    @DataProvider(name = "dataArrayArrayYAML")
    public Iterator<Object[]> dataArrayArrayYAML() throws Throwable {
        return DataProviders.createYamlFactory().createArrayStructured(getFilePath("TestYAMLArray.yaml"), int[].class).getData();
    }
    
    @DataProvider(name = "dataHashParamsYAML")
    public Iterator<Object[]> dataHashParamsYAML() throws Throwable {
        return DataProviders.createYamlFactory().createHashParams(getFilePath("TestYAMLHashArray.yaml")).getData();
    }
    
    @DataProvider(name = "dataHashArrayYAML")
    public Iterator<Object[]> dataHashArrayYAML() throws Throwable {
        return DataProviders.createYamlFactory().createHashStructured(getFilePath("TestYAMLHashArray.yaml"), int[].class).getData();
    }
    
    @Test(dataProvider = "dataDiningNodeYAML")
    public void testDiningNodeYAML(String name, JsonNode node) {
        JsonNode nlist = node.path("diningList");
        Iterator<String> i1 = new IteratorMap<JsonNode, String>(nlist.iterator()) {
            @Override
            public String apply(JsonNode o) {
                return o.asText();
            }
        };
        Iterator<String> i2 = new ArrayIterator(DINING_LIST);
        Assert.assertEquals(i1, i2);
        Assert.assertEquals(node.path("diningInfo").path("partySize").asInt(), 1);
    }
    
    @Test(dataProvider = "dataDiningClassYAML")
    public void testDiningClassYAML(String name, DiningTest node) {
        Iterator<String> i1 = node.diningList.iterator();
        Iterator<String> i2 = new ArrayIterator(DINING_LIST);
        Assert.assertEquals(i1, i2);
        Assert.assertEquals(node.diningInfo.partySize, PARTY_SIZE);
    }
    
    @Test(dataProvider = "dataArrayYAML")
    public void testArrayYAML(int a, int b, int c, int d, int e) {
        Assert.assertTrue(a == 1 || a == 5);
        Assert.assertTrue(b == 2 || b == 4);
        Assert.assertTrue(c == 3 || c == 3);
        Assert.assertTrue(d == 4 || d == 2);
        Assert.assertTrue(e == 5 || e == 1);
    }
    
    @Test(dataProvider = "dataArrayNodeYAML")
    public void testArrayNodeYAML(JsonNode a) {
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
    
    @Test(dataProvider = "dataArrayArrayYAML")
    public void testArrayArrayYAML(int[] a) {
        Assert.assertTrue(java.util.Arrays.equals(a, new int[] {1, 2, 3, 4, 5}) || java.util.Arrays.equals(a, new int[] { 5, 4, 3, 2, 1 }));
    }
    
    @Test(dataProvider = "dataHashParamsYAML")
    public void testHashParamsYAML(int a, int b, int c, int d, int e) {
        Assert.assertTrue(a == 1 || a == 5);
        Assert.assertTrue(b == 2 || b == 4);
        Assert.assertTrue(c == 3 || c == 3);
        Assert.assertTrue(d == 4 || d == 2);
        Assert.assertTrue(e == 5 || e == 1);
    }
    
    @Test(dataProvider = "dataHashArrayYAML")
    public void testHashArrayYAML(String name, int[] a) {
        Assert.assertTrue(java.util.Arrays.equals(a, new int[] {1, 2, 3, 4, 5}) || java.util.Arrays.equals(a, new int[] { 5, 4, 3, 2, 1 }));
    }
    
}
