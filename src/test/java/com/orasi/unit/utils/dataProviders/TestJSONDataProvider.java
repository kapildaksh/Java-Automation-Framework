/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.unit.utils.dataProviders;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.orasi.utils.Constants;
import com.orasi.utils.dataProviders.JSONDataProvider;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 *
 * @author brian.becker
 */
public class TestJSONDataProvider {
    
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

    @DataProvider(name = "dataDining")
    public Iterator<Object[]> dataDiningNode() throws Throwable {
        // System.out.println(getClass().getResource(Constants.BLUESOURCE_DATAPROVIDER_PATH + "TestJSONDining.json").toString());
	return new JSONDataProvider(getFilePath("TestJSONDining.json")).getDataMap(JsonNode.class);
    }
    
    @DataProvider(name = "dataDiningClass")
    public Iterator<Object[]> dataDiningNodeClass() throws Throwable {
        // System.out.println(getClass().getResource(Constants.BLUESOURCE_DATAPROVIDER_PATH + "TestJSONDining.json").toString());
	return new JSONDataProvider(getFilePath("TestJSONDining.json")).getDataMap(DiningTest.class);
    }
    
    @Test(dataProvider = "dataDining")
    public void testDiningNode(String name, JsonNode node) {
        System.out.println(name + ": " + node.path("diningList").toString());
    }
    
    @Test(dataProvider = "dataDiningClass")
    public void testDiningNodeClass(String name, DiningTest node) {
        System.out.println(name + ": " + node.diningList.toString());
    }
    
}
