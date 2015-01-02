/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.sandbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.orasi.utils.Constants;
import com.orasi.utils.dataProviders.JacksonDataProviderFactory;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class TestXMLDataProvider {
    
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

   /*@DataProvider(name = "dataDiningNode")
    public Iterator<Object[]> dataDiningNode() throws Throwable {
        return JacksonDataProviderFactory.getJsonFactory(getFilePath("TestJSONDining.json")).createHashNode().getData();
    }
    
    @DataProvider(name = "dataDiningClass")
    public Iterator<Object[]> dataDiningClass() throws Throwable {
        return JacksonDataProviderFactory.getJsonFactory(getFilePath("TestJSONDining.json")).createHashStructured(DiningTest.class).getData();
    }*/
    
    @DataProvider(name = "xmlDataArray")
    public Iterator<Object[]> dataXMLArray() throws Throwable {
        return JacksonDataProviderFactory.getXmlFactory(getFilePath("TestXMLArray.xml")).createArrayParams().getData();
    }
    
    @DataProvider(name = "xmlDataArrayNode")
    public Iterator<Object[]> dataXMLArrayNode() throws Throwable {
        return JacksonDataProviderFactory.getXmlFactory(getFilePath("TestXMLArray.xml")).createArrayNode().getData();
    }
    
    @DataProvider(name = "xmlDataArrayArray")
    public Iterator<Object[]> dataXMLArrayArray() throws Throwable {
        return JacksonDataProviderFactory.getXmlFactory(getFilePath("TestXMLArray.xml")).createArrayStructured(int[].class).getData();
    }
    
    /*@DataProvider(name = "xmlDataHashParams")
    public Iterator<Object[]> dataHashParams() throws Throwable {
        return JacksonDataProviderFactory.getXmlFactory(getFilePath("TestXMLHashArray.xml")).createHashParams().getData();
    }
    
    @DataProvider(name = "xmlDataHashArray")
    public Iterator<Object[]> dataHashArray() throws Throwable {
        return JacksonDataProviderFactory.getXmlFactory(getFilePath("TestXMLHashArray.xml")).createHashStructured(int[].class).getData();
    }*/
    
    /*@Test(dataProvider = "xmlDataDiningNode")
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
    
    @Test(dataProvider = "xmlDataDiningClass")
    public void testDiningClass(String name, DiningTest node) {
        Iterator<String> i1 = node.diningList.iterator();
        Iterator<String> i2 = new ArrayIterator(DINING_LIST);
        Assert.assertEquals(i1, i2);
        Assert.assertEquals(node.diningInfo.partySize, PARTY_SIZE);
    }*/
    
    @Test(dataProvider = "xmlDataArray")
    public void testXMLArray(Object a, Object b, Object c, Object d, Object e) { //Object a, Object b, Object c, Object d, Object e) {
        System.out.println(a);
        //ObjectMapper mapper = new XmlMapper();
        //try {
            //mapper.configure(DeserializationFeature., true)
            //mapper.writeValue(System.out, new int[] { 1, 2, 3, 4, 5 });
            //Assert.assertTrue(a == 1 || a == 5);
            //Assert.assertTrue(b == 2 || b == 4);
            //Assert.assertTrue(c == 3 || c == 3);
            //Assert.assertTrue(d == 4 || d == 2);
            //Assert.assertTrue(e == 5 || e == 1);
        //} catch (IOException ex) {
        //    Logger.getLogger(TestXMLDataProvider.class.getName()).log(Level.SEVERE, null, ex);
        //}
    }
    
    /*@Test(dataProvider = "xmlDataArrayNode")
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
    }*/
    
    @Test(dataProvider = "xmlDataArrayArray")
    public void testXMLArrayArray(int[] a) {
        Assert.assertTrue(java.util.Arrays.equals(a, new int[] {1, 2, 3, 4, 5}) || java.util.Arrays.equals(a, new int[] { 5, 4, 3, 2, 1 }));
    }
    
    /*@Test(dataProvider = "xmlDataHashParams")
    public void testHashParams(int a, int b, int c, int d, int e) {
        Assert.assertTrue(a == 1 || a == 5);
        Assert.assertTrue(b == 2 || b == 4);
        Assert.assertTrue(c == 3 || c == 3);
        Assert.assertTrue(d == 4 || d == 2);
        Assert.assertTrue(e == 5 || e == 1);
    }
    
    @Test(dataProvider = "xmlDataHashArray")
    public void testHashArray(String name, int[] a) {
        Assert.assertTrue(java.util.Arrays.equals(a, new int[] {1, 2, 3, 4, 5}) || java.util.Arrays.equals(a, new int[] { 5, 4, 3, 2, 1 }));
    }*/
    
}
