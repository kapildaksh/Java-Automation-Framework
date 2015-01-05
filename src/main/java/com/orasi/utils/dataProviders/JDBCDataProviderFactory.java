/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.dataProviders;

import java.nio.file.Path;

/**
 * This is a JDBCDataProviderFactory, which will create various data
 * providers which utilize a SQL database.
 * 
 * @author Brian Becker
 */
public class JDBCDataProviderFactory {
    
    public enum ConnectionUrlType {
        FILE, SERVER
    }
    
    private final ConnectionUrlType urlType;
    private final String dbClass;
    private final String dbSpec;
    private String dbUrl;
    
    /**
     * Create a new factory which creates a given type of database specified
     * by the dbSpec string.
     * 
     * @param urlType
     * @param dbClass
     * @param dbSpec 
     */
    public JDBCDataProviderFactory(ConnectionUrlType urlType, String dbClass, String dbSpec) {
        this.urlType = urlType;
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
    public JDBCDataProvider createFileTable(Path filePath, String table) throws Throwable {
        if(this.urlType != ConnectionUrlType.FILE)
            throw new UnsupportedOperationException("This database does not support files");
        Class.forName(this.dbClass);
        this.dbUrl = this.dbSpec + filePath.toString();
        return new JDBCDataProvider(this.dbUrl, table, null, null);
    }
    
    /**
     * Create a new JDBCDataProvider which uses the given host, db, and table.
     * 
     * @param host      Hostname
     * @param db        Database Name
     * @param table     Table
     * @return
     * @throws java.lang.Throwable
     */
    public JDBCDataProvider createServerTable(String host, String db, String table) throws Throwable {
        if(this.urlType != ConnectionUrlType.SERVER)
            throw new UnsupportedOperationException("This database does not support servers");
        Class.forName(this.dbClass);
        this.dbUrl = this.dbSpec + "//" + host + "/" + db;
        return new JDBCDataProvider(this.dbUrl, table, null, null);
    }
    
}
