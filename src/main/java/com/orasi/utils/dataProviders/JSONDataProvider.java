package com.orasi.utils.dataProviders;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orasi.utils.types.IteratorMap;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

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
    public JSONDataProvider(String filePath) throws Throwable {
        this.mapData = Files.readAllBytes(Paths.get(filePath));
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
     * @throws  java.lang.Throwable
     */
    public Iterator<Object[]> getData() throws Throwable {
        ObjectMapper map = new ObjectMapper();
        final Iterator<ArrayList<Object>> data = map.readValue(mapData, LinkedList.class).listIterator();
        return new IteratorMap<ArrayList<Object>, Object[]>(data) {
            @Override
            public Object[] apply(ArrayList<Object> objs) {
                return objs.toArray();
            }
        };
    }

    /**
     * This gets the test data from a JSON file.  It returns all the data 
     * as an Iterator of Object[]. It accepts an associative array and
     * passes the key name and value to the test function. It is up to the
     * test function to query the value.
     * 
     * {@code
     *      {
     *          "TestCase1":
     *              [ "Col1", "Col2", "Col3", "Col4" ],
     *          "TestCase2":
     *              [ 1, 2, 3, 4 ],
     *          "TestCase3":
     *              {
     *                  "A": 1,
     *                  "B": 2
     *              }
     *      }
     * }
     * 
     * @version	12/30/2014
     * @author 	Brian Becker
     * @return 	Iterator of Object[]
     * @throws  java.lang.Throwable
     */
    public Iterator<Object[]> getDataMap() throws Throwable {
        ObjectMapper map = new ObjectMapper();
        final JsonNode root = map.readValue(mapData, JsonNode.class);
        return new IteratorMap<String, Object[]>(root.fieldNames()) {
            @Override
            public Object[] apply(String o) {
                return new Object[] { o, root.get(o) };
            }
        };
    }

}