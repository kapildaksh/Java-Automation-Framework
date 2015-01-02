/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.sandbox;

import com.orasi.utils.Constants;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
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
public class TestSQLiteDataProvider {
    
    public Path getFilePath(String name) throws Throwable {
        return Paths.get(getClass().getResource(Constants.BLUESOURCE_DATAPROVIDER_PATH).toURI()).resolve(name);
    }

    //@DataProvider(name = "quickTestSQLite")
    //public Iterator<Object[]> dataQuickTestSQLite() throws Throwable {
    //    // return JacksonDataProviderFactory.getCsvFactory(getFilePath("QuickTestSQLite.db")).createArrayParams().getData();
    //}
    
    @Test(dataProvider = "quickTestSQLite")
    public void testQuickTestSQLite(String a, String b, String c, String d, String e) {
        Assert.assertTrue("1".equals(a) || "5".equals(a));
    }
    
}
