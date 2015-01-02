/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.dataProviders;

import java.nio.file.Path;
import org.sqlite.SQLiteDataSource;

/**
 * This is a JDBCDataProviderFactory, which will create various data
 * providers which utilize a SQL database.
 * 
 * @author Brian Becker
 */
public class JDBCDataProviderFactory {
    
    /**
     * Create a new JDBCDataProvider which uses a SQLite database as storage.
     * 
     * @param filePath  Path to data file
     * @param table
     * @return 
     */
    public static JDBCDataProvider createSQLite(Path filePath, String table) {
        SQLiteDataSource ds = new SQLiteDataSource();
        System.out.println(filePath.toString());
        ds.setUrl("jdbc:sqlite:" + filePath.toString());
        return new JDBCDataProvider(ds, table, null, null);
    }
    
}
