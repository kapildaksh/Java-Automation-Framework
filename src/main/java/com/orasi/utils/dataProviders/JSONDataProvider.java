package com.orasi.utils.dataProviders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
//import org.codehaus.jackson.JsonParser;

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
     * This gets the test data from a csv file.  It returns all the data 
     * as a 2d array
     * 
     * @param	filePath		the file path of the CSV file
     * @version	12/30/2014
     * @author 	Brian Becker
     * @return 	Iterator of Object[]
     * @throws  java.lang.Throwable
     */
    public Iterator<Object[]> getTestScenarioData(String filePath) throws Throwable {
        ObjectMapper map = new ObjectMapper();
        Collection<Object[]> list = new LinkedList<>();
        Iterator<Object[]> data = map.readValue(this.mapData, Iterator.class);
        return data;
    }

}
