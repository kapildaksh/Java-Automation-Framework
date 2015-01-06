/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.types;

import com.orasi.utils.dataProviders.JDBCDataProvider;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The ResultSet iterator takes a ResultSet and converts it to an Iterator of
 * Object[].
 * 
 * @author Brian Becker
 */
public class IteratorResultSet implements Iterator {
    private boolean hNext = true;
    private final ResultSet rs;
    
    public IteratorResultSet(ResultSet rs) {
        this.rs = rs;
        try {
            this.hNext = this.rs.next();
        } catch (SQLException ex) {
            this.hNext = false;
            Logger.getLogger(IteratorResultSet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public boolean hasNext() {
        return this.hNext;
    }

    @Override
    public Object[] next() {
        try {
            int cols = rs.getMetaData().getColumnCount();
            List<Object> items = new ArrayList<>();
            for(int i = 1; i <= cols; i++) {
                items.add(rs.getObject(i));
            }
            this.hNext = rs.next();
            return items.toArray();
        } catch (SQLException ex) {
            Logger.getLogger(JDBCDataProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Write to test database not supported.");
    }
}
