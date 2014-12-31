/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.unit.utils.dataProviders;

import com.fasterxml.jackson.databind.JsonNode;
import com.orasi.utils.Constants;
import com.orasi.utils.dataProviders.JSONDataProvider;
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
public class TestJSONDataProvider {
    
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
        return Paths.get(getClass().getResource(Constants.BLUESOURCE_DATAPROVIDER_PATH + name).toURI());
    }

    @DataProvider(name = "dataDiningNode")
    public Iterator<Object[]> dataDiningNode() throws Throwable {
        // System.out.println(getClass().getResource(Constants.BLUESOURCE_DATAPROVIDER_PATH + "TestJSONDining.json").toString());
	return new JSONDataProvider(getFilePath("TestJSONDining.json")).getDataMap(JsonNode.class);
    }
    
    @DataProvider(name = "dataDiningClass")
    public Iterator<Object[]> dataDiningClass() throws Throwable {
        // System.out.println(getClass().getResource(Constants.BLUESOURCE_DATAPROVIDER_PATH + "TestJSONDining.json").toString());
	return new JSONDataProvider(getFilePath("TestJSONDining.json")).getDataMap(DiningTest.class);
    }
    
    @Test(dataProvider = "dataDiningNode")
    public void testDiningNode(String name, JsonNode node) {
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
    
    @Test(dataProvider = "dataDiningClass")
    public void testDiningClass(String name, DiningTest node) {
        Iterator<String> i1 = node.diningList.iterator();
        Iterator<String> i2 = new ArrayIterator(DINING_LIST);
        Assert.assertEquals(i1, i2);
        Assert.assertEquals(node.diningInfo.partySize, PARTY_SIZE);
    }
    
}
