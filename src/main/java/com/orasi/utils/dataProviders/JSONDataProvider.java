package com.orasi.utils.dataProviders;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orasi.utils.types.IteratorMap;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is a basic JSON data provider, which will take in a few formats
 * of JSON file.
 * 
 * @version     12/30/2014
 * @author      Brian Becker
 */
public class JSONDataProvider {
    
    // File path of JSON data
    private final byte[] mapData;
    
    /**
     * This creates the JSON Data provider with a given file path.
     * 
     * @param       filePath                file path of the JSON file
     * @version     12/30/2014
     * @author      Brian Becker
     * @throws      java.lang.Throwable
     */
    public JSONDataProvider(Path filePath) throws Throwable {
        this.mapData = Files.readAllBytes(filePath);
    }
    
    /**
     * This gets the test data from a JSON file.  It returns all the data 
     * as an Iterator of Object[]. It accepts a 2d array JSON file.
     * 
     * {@code
     *      [
     *          [ "Col1", "Col2", "Col3", "Col4" ],
     *          [ 1, 2, 3, 4 ]
     *      ]
     * }
     * 
     * @version	12/30/2014
     * @author 	Brian Becker
     * @return 	Iterator of Object[]
     */
    public Iterator<Object[]> getData() {
        try {
            ObjectMapper map = new ObjectMapper();
            final Iterator<ArrayList<Object>> data = map.readValue(mapData, LinkedList.class).listIterator();
            return new IteratorMap<ArrayList<Object>, Object[]>(data) {
                @Override
                public Object[] apply(ArrayList<Object> objs) {
                    return objs.toArray();
                }
            };
        } catch (IOException ex) {
            Logger.getLogger(JSONDataProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Collections.EMPTY_LIST.iterator();
    }

    /**
     * This gets the test data from a JSON file.  It returns all the data 
     * as an Iterator of Object[]. It accepts an associative array and
     * passes the key name and value to the test function. A structure is
     * needed for parsing the data.
     * 
     * {@code
     *      {
     *          "TestCase1":
     *              { "Col1": 1, "Col2": 2, "Col3": 3, "Col4": 4 },
     *          "TestCase2":
     *              { "Col5": 5, "Col6": 6, "Col7": 7, "Col8": 8 }
     *      }
     * }
     * 
     * @version	12/30/2014
     * @author 	Brian Becker
     * @param   structure               provide a class which defines a test case instance
     * @return 	Iterator of Object[]
     */
    public Iterator<Object[]> getDataMap(final Class structure) { try {
        //throws Throwable {
        ObjectMapper map = new ObjectMapper();
        map.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JavaType type = map.getTypeFactory().constructMapType(HashMap.class, String.class, structure);
        final HashMap<Object, Object> data = map.readValue(mapData, type);
        return new IteratorMap<Object, Object[]>(data.keySet().iterator()) {
            @Override
            public Object[] apply(Object o) {
                return new Object[] { o, structure.cast(data.get(o)) };
            }
        };
        } catch (IOException ex) {
            Logger.getLogger(JSONDataProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Collections.EMPTY_LIST.iterator();
    }

}