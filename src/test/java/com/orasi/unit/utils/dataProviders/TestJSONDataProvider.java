/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.unit.utils.dataProviders;

import com.fasterxml.jackson.databind.JsonNode;
import com.orasi.utils.Constants;
import com.orasi.utils.dataProviders.JSONDataProvider;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 *
 * @author brian.becker
 */
public class TestJSONDataProvider {
    
    public Path getFilePath(String name) throws Throwable {
        return Paths.get(getClass().getResource(Constants.BLUESOURCE_DATAPROVIDER_PATH + name).toURI());
    }

    @DataProvider(name = "dataDining")
    public Iterator<Object[]> dataDiningNode() throws Throwable {
        // System.out.println(getClass().getResource(Constants.BLUESOURCE_DATAPROVIDER_PATH + "TestJSONDining.json").toString());
	return new JSONDataProvider(getFilePath("TestJSONDining.json")).getDataMap(JsonNode.class);
    }
    
    @Test(dataProvider = "dataDining")
    public void testDiningNode(String name, JsonNode node) {
        System.out.println(name + ": " + node.path("diningList").toString());
    }
    
}
