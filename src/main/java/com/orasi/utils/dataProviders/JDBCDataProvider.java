/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.dataProviders;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.lang.StringEscapeUtils;
import org.sqlite.SQLiteDataSource;
import org.sqlite.SQLiteJDBCLoader;

/**
 * This is a database data provider. It takes a URL for a given SQL server
 * and a table name. It can be used with any database that is supported by
 * the Java JDBC API.
 * 
 * @author Brian Becker
 */
public class JDBCDataProvider implements DataProvider {
    private final DataSource dataSource;
    private final String table;
    private final String user;
    private final String pass;

    public JDBCDataProvider(DataSource dataSource, String table, String user, String pass) {
        this.dataSource = dataSource;
        this.table = table;
        this.user = user;
        this.pass = pass;
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
            final Connection conn = this.user != null ? this.dataSource.getConnection(this.user, this.pass) : this.dataSource.getConnection();
            final PreparedStatement ps = conn.prepareStatement( "select * from " + StringEscapeUtils.escapeSql(this.table) );
            final ResultSet rs = ps.executeQuery();
            return new Iterator<Object[]>() {
                @Override
                public boolean hasNext() {
                    try {
                        return !rs.isLast();
                    } catch (SQLException ex) {
                        Logger.getLogger(JDBCDataProvider.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return false;
                }

                @Override
                public Object[] next() {
                    try {
                        rs.next();
                        int cols = rs.getMetaData().getColumnCount();
                        List<Object> items = new ArrayList<>();
                        for(int i = 1; i <= cols; i++) {
                            items.add(rs.getObject(i));
                        }
                        return items.toArray();
                    } catch (SQLException ex) {
                        Logger.getLogger(JDBCDataProvider.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return null;
                }

                @Override
                public void remove() {
                    throw new UnsupportedOperationException("Write to test database not supported."); //To change body of generated methods, choose Tools | Templates.
                }
            };
        } catch (Throwable ex) {
            Logger.getLogger(JacksonDataProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Collections.EMPTY_LIST.iterator();
    }

}
