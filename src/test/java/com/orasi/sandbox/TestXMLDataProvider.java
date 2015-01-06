/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.sandbox;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.orasi.utils.Constants;
import com.orasi.utils.dataProviders.DataProviders;
import com.orasi.utils.types.IteratorMap;
import edu.emory.mathcs.backport.java.util.Collections;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.iterators.ArrayIterator;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * These are some basic tests for the XML Data Provider, in both node based
 * traversal and a class mapping. This allows passing very large and complex
 * structures into test cases for things such as web services.
 * 
 * NOTE: Where XML itself is the structure that should be passed into a test
 * case, you should escape it and use any data provider you like. This is for
 * passing object-like data to test cases.
 * 
 * @author Brian Becker
 */
public class TestXMLDataProvider {
    
    private static final String[] DINING_LIST = { "napa-rose", "goofys-kitchen" };
    private static final int PARTY_SIZE = 1;
    
    public static class DiningTest {
        public Map<String,String> diningList;
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

    @DataProvider(name = "xmlDataDiningNode")
    public Iterator<Object[]> dataXMLDiningNode() throws Throwable {
        return DataProviders.createXmlFactory().createHashNode(getFilePath("TestXMLDining.xml")).getData();
    }
    
    @DataProvider(name = "xmlDataDiningClass")
    public Iterator<Object[]> dataXMLDiningClass() throws Throwable {
        return DataProviders.createXmlFactory().createHashStructured(getFilePath("TestXMLDining.xml"), DiningTest.class).getData();
    }
    
    @DataProvider(name = "xmlDataArray")
    public Iterator<Object[]> dataXMLArray() throws Throwable {
        return DataProviders.createXmlFactory().createArrayParams(getFilePath("TestXMLArray.xml")).getData();
    }
    
    @DataProvider(name = "xmlDataArrayNode")
    public Iterator<Object[]> dataXMLArrayNode() throws Throwable {
        return DataProviders.createXmlFactory().createArrayNode(getFilePath("TestXMLArray.xml")).getData();
    }
    
    @DataProvider(name = "xmlDataArrayArray")
    public Iterator<Object[]> dataXMLArrayArray() throws Throwable {
        return DataProviders.createXmlFactory().createArrayStructured(getFilePath("TestXMLArray.xml"), int[].class).getData();
    }
    
    @DataProvider(name = "xmlDataHashParams")
    public Iterator<Object[]> dataXMLHashParams() throws Throwable {
        return DataProviders.createXmlFactory().createHashParams(getFilePath("TestXMLHashArray.xml")).getData();
    }
    
    @DataProvider(name = "xmlDataHashArray")
    public Iterator<Object[]> dataXMLHashArray() throws Throwable {
        return DataProviders.createXmlFactory().createHashStructured(getFilePath("TestXMLHashArray.xml"), int[].class).getData();
    }
    
    @Test(dataProvider = "xmlDataDiningNode")
    public void testXMLDiningNode(JsonNode node) {
        JsonNode nlist = node.path("diningList");
        Iterator<String> i1 = new IteratorMap<JsonNode, String>(nlist.iterator()) {
            @Override
            public String apply(JsonNode o) {
                return o.asText();
            }
        };
        
        List<String> l1 = Lists.newArrayList(i1);
        List<String> l2 = Lists.newArrayList(DINING_LIST);

        Assert.assertFalse(Collections.disjoint(l1, l2));
        Assert.assertEquals(node.path("diningInfo").path("partySize").asInt(), 1);
    }
    
    @Test(dataProvider = "xmlDataDiningClass")
    public void testXMLDiningClass(DiningTest node) {
        Assert.assertFalse(Collections.disjoint(node.diningList.values(), Lists.newArrayList(DINING_LIST)));
    }
    
    @Test(dataProvider = "xmlDataArray")
    public void testXMLArray(String a, String b, String c, String d, String e) {
        Assert.assertTrue("1".equals(a) || "5".equals(a));
        Assert.assertTrue("2".equals(b) || "4".equals(b));
        Assert.assertTrue("3".equals(c) || "3".equals(c));
        Assert.assertTrue("4".equals(d) || "2".equals(d));
        Assert.assertTrue("5".equals(e) || "1".equals(e));
    }
    
    @Test(dataProvider = "xmlDataArrayNode")
    public void testXMLArrayNode(JsonNode a) {           
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
    
    @Test(dataProvider = "xmlDataArrayArray")
    public void testXMLArrayArray(int[] a) {
        Assert.assertTrue(java.util.Arrays.equals(a, new int[] {1, 2, 3, 4, 5}) || java.util.Arrays.equals(a, new int[] { 5, 4, 3, 2, 1 }));
    }
    
    @Test(dataProvider = "xmlDataHashParams")
    public void testXMLHashParams(String a, String b, String c, String d, String e) {
        Assert.assertTrue("1".equals(a) || "5".equals(a));
        Assert.assertTrue("2".equals(b) || "4".equals(b));
        Assert.assertTrue("3".equals(c) || "3".equals(c));
        Assert.assertTrue("4".equals(d) || "2".equals(d));
        Assert.assertTrue("5".equals(e) || "1".equals(e));
    }
    
    @Test(dataProvider = "xmlDataHashArray")
    public void testXMLHashArray(int[] a) {
        Assert.assertTrue(java.util.Arrays.equals(a, new int[] {1, 2, 3, 4, 5}) || java.util.Arrays.equals(a, new int[] { 5, 4, 3, 2, 1 }));
    }
    
}
