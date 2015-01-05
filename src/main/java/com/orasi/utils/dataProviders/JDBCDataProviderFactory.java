/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.dataProviders;

import java.nio.file.Path;
import java.sql.DriverManager;
import javax.sql.DataSource;
import org.sqlite.SQLiteDataSource;

/**
 * This is a JDBCDataProviderFactory, which will create various data
 * providers which utilize a SQL database.
 * 
 * @author Brian Becker
 */
public class JDBCDataProviderFactory {
    
    private final String dbClass;
    private final String dbSpec;
    private String dbUrl;
    
    /**
     * Create a new factory which creates a given type of database specified
     * by the dbSpec string.
     * 
     * @param dbClass
     * @param dbSpec 
     */
    public JDBCDataProviderFactory(String dbClass, String dbSpec) {
        this.dbClass = dbClass;
        this.dbSpec = dbSpec;
    }
       
    /**
     * Create a new JDBCDataProvider which uses the given path and table.
     * 
     * @param filePath  Path to data file
     * @param table     Table to dump data from
     * @return 
     * @throws java.lang.Throwable 
     */
    public JDBCDataProvider createTable(Path filePath, String table) throws Throwable {
        Class.forName(this.dbClass);
        this.dbUrl = "jdbc:" + this.dbSpec + ":" + filePath.toString();
        return new JDBCDataProvider(this.dbUrl, table, null, null);
    }
    
}
