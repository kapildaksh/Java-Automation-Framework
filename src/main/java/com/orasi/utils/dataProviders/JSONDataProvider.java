package com.orasi.utils.dataProviders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orasi.utils.types.IteratorMap;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

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
     * as an Iterator of Object[].
     * 
     * @version	12/30/2014
     * @author 	Brian Becker
     * @return 	Iterator of Object[]
     * @throws  java.lang.Throwable
     */
    public Iterator<Object[]> getTestScenarioData() throws Throwable {
        ObjectMapper map = new ObjectMapper();
        final Iterator<ArrayList<Object>> data = map.readValue(this.mapData, LinkedList.class).listIterator();
        return new IteratorMap<ArrayList<Object>, Object[]>(data) {
            @Override
            public Object[] next() {
                return data.next().toArray();
            }
        };
    }

}