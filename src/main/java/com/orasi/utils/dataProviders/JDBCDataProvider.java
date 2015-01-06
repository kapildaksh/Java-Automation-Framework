/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.dataProviders;

import com.orasi.utils.types.IteratorResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringEscapeUtils;

/**
 * This is a database data provider. It takes a URL for a given SQL server
 * and a table name. It can be used with any database that is supported by
 * the Java JDBC API.
 * 
 * @version 01/06/2015
 * @author  Brian Becker
 */
public class JDBCDataProvider implements DataProvider {
    private final String dataSourceUrl;
    private final String table;

    /**
     * Create a new JDBC Data Provider, with a data source URL, a table,
     * and a username and password to authenticate with.
     * 
     * @version 01/06/2015
     * @author  Brian Becker
     * @param   dataSourceUrl
     * @param   table
     */
    public JDBCDataProvider(String dataSourceUrl, String table) {
        this.dataSourceUrl = dataSourceUrl;
        this.table = table;
    }
        
    /**
     * This retrieves the data from the database engine, but it iterates over
     * a result set rather than dumping everything in memory to a list, which
     * would be extremely slow.
     * 
     * @version	01/02/2015
     * @author 	Brian Becker
     * @warning Handles exceptions, returning empty iterators on failure
     * @return 	Iterator of Object[]
     */
    @Override
    public Iterator<Object[]> getData() {
        try {
            final Connection conn = DriverManager.getConnection(this.dataSourceUrl);
            final PreparedStatement ps = conn.prepareStatement( "select * from " + StringEscapeUtils.escapeSql(this.table) );
            final ResultSet rs = ps.executeQuery();
            return new IteratorResultSet(rs);
        } catch (SQLException ex) {
            Logger.getLogger(JDBCDataProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Collections.EMPTY_LIST.iterator();
    }
}
