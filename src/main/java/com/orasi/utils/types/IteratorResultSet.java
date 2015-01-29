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
 * @version     1/06/2015
 * @author      Brian Becker
 */
public class IteratorResultSet implements Iterator {
    private boolean hNext = true;
    private final ResultSet rs;
    
    /**
     * Create an iterator over a ResultSet.
     * 
     * @param rs 
     */
    public IteratorResultSet(ResultSet rs) {
        this.rs = rs;
        try {
            this.hNext = this.rs.next();
        } catch (SQLException ex) {
            this.hNext = false;
            Logger.getLogger(IteratorResultSet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Are there any more results in the database return values?
     * 
     * @return 
     */
    @Override
    public boolean hasNext() {
        return this.hNext;
    }

    /**
     * Determine if there are any more items in the result set, and if so
     * then return an object array containing the values from the database.
     * Each of the objects will be typed, but will have to be inspected.
     * Fortunately, TestNG data providers will automatically cast the values
     * because they expect an object array.
     * 
     * @return 
     */
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

    /**
     * We can't remove values from the result set. This is just a view for the
     * database.
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Write to test database not supported.");
    }
}
