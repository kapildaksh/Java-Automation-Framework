/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.sandbox;

import com.orasi.utils.Constants;
import com.orasi.utils.dataProviders.DataProviders;
import com.orasi.utils.dataProviders.JDBCDataProvider;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 *
 * @author brian.becker
 */
public class TestExcelDataProvider {

    public Path getFilePath(String name) throws Throwable {
        return Paths.get(getClass().getResource(Constants.BLUESOURCE_DATAPROVIDER_PATH).toURI()).resolve(name);
    }
    
    @DataProvider(name = "quickTestExcel")
    public Iterator<Object[]> dataQuickTestExcel() throws Throwable {
        return DataProviders.createExcel(getFilePath("TestAddNewTitle.xlsx"), "TestAddNewTitle").getData(); //getFilePath("TestAddNewTitle.xlsx"), "TestAddNewTitle").getData();
    }
    
    @Test(dataProvider = "quickTestExcel")
    public void testQuickTestExcel(Object a, Object b, Object c) {
        Assert.assertEquals(a, "Add a new title");
        Assert.assertEquals(b, "COMPANY_ADMIN");
        Assert.assertEquals(c, "Test1234");
    }
    
}
