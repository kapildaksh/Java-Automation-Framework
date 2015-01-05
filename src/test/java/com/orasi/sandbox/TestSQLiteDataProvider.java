/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.sandbox;

import com.orasi.utils.Constants;
import com.orasi.utils.dataProviders.DataProviderFactory;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
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

    @DataProvider(name = "quickTestSQLite")
    public Iterator<Object[]> dataQuickTestSQLite() throws Throwable {
        return DataProviderFactory.getJdbcFactory().createSQLite(getFilePath("TestSQLiteQuick.db"), "numbers").getData();
    }
    
    @Test(dataProvider = "quickTestSQLite")
    public void testQuickTestSQLite(Object a, Object b, Object c, Object d, Object e) {
        System.out.println(a + " " + b + " " + c + " " + d + " " + e);
    }
    
}
