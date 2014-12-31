package com.orasi.utils.dataProviders;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orasi.utils.types.IteratorMap;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is a basic JSON data provider. Instances can be held in either a 2d
 * array or in an associative array. Instances themselves can either be 2d
 * arrays of "objects" which are passed as parameters to the test case, or
 * they may be structured data. Structured data can be accessed either through
 * the JsonNode object, or custom objects which you provide to this class.
 * 
 * NOTE: Where JSON itself is the structure that should be passed into a test
 * case, you should escape it and use any data provider you like. This is for
 * passing object-like data to test cases.
 * 
 * @version     12/30/2014
 * @author      Brian Becker
 */
public class JSONDataProvider {
    
    private final ObjectMapper map;
    private final JavaType dataType;
    
    // File path of JSON data
    private final byte[] mapData;
    
    /**
     * This creates the JSON Data provider with a given file path, instance
     * structure, as well as instance array or hash map.
     * 
     * @param       filePath                file path of the JSON file
     * @version     12/30/2014
     * @author      Brian Becker
     * @param       map                     object mapper used to create type
     * @param       dataType                data type of test
     * @throws      java.lang.Throwable
     */
    public JSONDataProvider(Path filePath, ObjectMapper map, JavaType dataType) throws Throwable {
        this.mapData = Files.readAllBytes(filePath);
        this.map = map;
        this.dataType = dataType;
    }
    
    public static JSONDataProvider createArrayParams(Path filePath) throws Throwable {
        ObjectMapper map = new ObjectMapper();
        //JavaType is = map.getTypeFactory().constructType(Object.class);
        JavaType dt = map.getTypeFactory().constructArrayType(map.getTypeFactory().constructArrayType(Object.class));
        return new JSONDataProvider(filePath, map, dt);
    }
    
    public static JSONDataProvider createArrayNode(Path filePath) throws Throwable {
        ObjectMapper map = new ObjectMapper();
        JavaType dt = map.getTypeFactory().constructArrayType(map.getTypeFactory().constructArrayType(JsonNode.class));
        return new JSONDataProvider(filePath, map, dt);
    }
    
    public static JSONDataProvider createArrayStructured(Path filePath, Class structure) throws Throwable {
        ObjectMapper map = new ObjectMapper();
        map.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JavaType dt = map.getTypeFactory().constructArrayType(map.getTypeFactory().constructArrayType(structure));
        return new JSONDataProvider(filePath, map, dt);
    }
    
    public static JSONDataProvider createHashParams(Path filePath) throws Throwable {
        ObjectMapper map = new ObjectMapper();
        JavaType dt = map.getTypeFactory().constructMapType(HashMap.class, map.getTypeFactory().constructType(String.class), map.getTypeFactory().constructArrayType(Object.class));
        return new JSONDataProvider(filePath, map, dt);
    }
    
    public static JSONDataProvider createHashNode(Path filePath) throws Throwable {
        ObjectMapper map = new ObjectMapper();
        JavaType dt = map.getTypeFactory().constructMapType(HashMap.class, String.class, JsonNode.class);
        return new JSONDataProvider(filePath, map, dt);
    }
    
    public static JSONDataProvider createHashStructured(Path filePath, Class structure) throws Throwable {
        ObjectMapper map = new ObjectMapper();
        map.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JavaType dt = map.getTypeFactory().constructMapType(HashMap.class, String.class, structure);
        return new JSONDataProvider(filePath, map, dt);        
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
     * @return 	Iterator of Object[]
     */
    public Iterator<Object[]> getData() {
        try {
            Iterator<Object[]> ito = this.map.readValue(mapData, dataType);
            System.out.println("Whoops");
            return ito;
        } catch (IOException ex) {
            Logger.getLogger(JSONDataProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Collections.EMPTY_LIST.iterator();
    }

}