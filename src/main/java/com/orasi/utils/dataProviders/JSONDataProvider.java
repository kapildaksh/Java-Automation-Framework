package com.orasi.utils.dataProviders;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.orasi.utils.types.IteratorMap;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.collections.iterators.ArrayIterator;

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
    
    // Keep an instance of the object mapper around for reading the data
    private final ObjectMapper map;
    // Data type which the provider is going to use to map json
    private final JavaType dataType;
    
    // JSON data
    private final byte[] mapData;
    // Wrapping parameters for non-params based method calls
    private final boolean wrapParams;
    
    /**
     * This creates the JSON Data provider with a given file path, instance
     * structure, as well as instance array or hash map.
     * 
     * @param       filePath                file path of the JSON file
     * @version     12/30/2014
     * @author      Brian Becker
     * @param       map                     object mapper used to create type
     * @param       dataType                data type of test
     * @param       wrapParams              parameter wrapping needed
     * @throws      java.lang.Throwable
     */
    public JSONDataProvider(Path filePath, ObjectMapper map, JavaType dataType, boolean wrapParams) throws Throwable {
        this.mapData = Files.readAllBytes(filePath);
        this.map = map;
        this.dataType = dataType;
        this.wrapParams = wrapParams;
    }
    
    /**
     * This is an array with params test case entries. The entries must be
     * of an array type, but the array elements themselves may be any
     * primitive type.
     * 
     * @param filePath      Path of JSON file
     * @return
     * @throws Throwable 
     */
    public static JSONDataProvider createArrayParams(Path filePath) throws Throwable {
        ObjectMapper map = new ObjectMapper();
        JavaType dt = map.getTypeFactory().constructArrayType(map.getTypeFactory().constructArrayType(Object.class));
        return new JSONDataProvider(filePath, map, dt, false);
    }
    
    /**
     * This is an array with node test case entries. You may use special
     * functions such as .path("elementName") to traverse and retrieve values.
     * See the Jackson API for more details regarding the JsonNode Class.
     * 
     * @param filePath      Path of JSON file
     * @return
     * @throws Throwable 
     */
    public static JSONDataProvider createArrayNode(Path filePath) throws Throwable {
        return createArrayStructured(filePath, JsonNode.class);
    }
    
    /**
     * This is an array with structured test case entries. The structured
     * entries can be any class. You may use int[].class if you want a numeric
     * array, for instance, or you can use a user-defined class.
     * 
     * The test methods will be called with the following parameter spec:
     * testFunction([structure] value);
     * 
     * @param   filePath      Path of JSON file
     * @param   structure     Structure of JSON instance entries
     * @return
     * @throws  Throwable 
     */
    public static JSONDataProvider createArrayStructured(Path filePath, Class structure) throws Throwable {
        ObjectMapper map = new ObjectMapper();
        map.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JavaType dt = map.getTypeFactory().constructArrayType(structure);
        return new JSONDataProvider(filePath, map, dt, true);
    }
    
    /**
     * This is a hash table with params test case entries. The entries must be
     * of an array type, but the array elements themselves may be any
     * primitive type.
     * 
     * @param   filePath      Path of JSON file
     * @return
     * @throws  Throwable 
     */
    public static JSONDataProvider createHashParams(Path filePath) throws Throwable {
        ObjectMapper map = new ObjectMapper();
        JavaType dt = map.getTypeFactory().constructMapType(HashMap.class, map.getTypeFactory().constructType(String.class), map.getTypeFactory().constructArrayType(Object.class));
        return new JSONDataProvider(filePath, map, dt, false);
    }
    
    /**
     * This is a hash table with node test case entries. You may use special
     * functions such as .path("elementName") to traverse and retrieve values.
     * See the Jackson API for more details regarding the JsonNode Class.
     *
     * @param   filePath      Path of JSON file
     * @return
     * @throws  Throwable 
     */
    public static JSONDataProvider createHashNode(Path filePath) throws Throwable {
        return createHashStructured(filePath, JsonNode.class);
    }
    
    /**
     * This is a hash table with structured test case entries. The structured
     * entries can be any class. You may use int[].class if you want a numeric
     * array, for instance, or you can use a user-defined class.
     * 
     * The test methods will be called with the following parameter spec:
     * testFunction([structure] value);
     * 
     * @param   filePath      Path of JSON file
     * @param   structure     Structure of JSON instance entries
     * @return
     * @throws  Throwable 
     */
    public static JSONDataProvider createHashStructured(Path filePath, Class structure) throws Throwable {
        ObjectMapper map = new ObjectMapper();
        map.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JavaType dt = map.getTypeFactory().constructMapType(HashMap.class, String.class, structure);
        return new JSONDataProvider(filePath, map, dt, true);        
    }

    /**
     * This gets the test data from a JSON file, given the file is in an
     * appropriate format for the JSON provider configuration. The outer
     * level must either be an array or a hash map, because it must be
     * able to be iterated through.
     * 
     * Example of JSONDataProvider configured to output parameters from a
     * JSON file with a root-level hash table.
     * {@code
     *      {
     *          "TestCase1":
     *              [1, 2, 3, 4, 5],
     *          "TestCase2":
     *              [5, 4, 3, 2, 1]
     *      }
     * }
     * 
     * @version	12/30/2014
     * @author 	Brian Becker
     * @warning Handles exceptions, returning empty iterators on failure
     * @return 	Iterator of Object[]
     */
    public Iterator<Object[]> getData() {
        try {
            Object is = this.map.readValue(mapData, dataType);
            if(is instanceof HashMap) {
                HashMap hm = (HashMap) is;
                return new IteratorMap<Entry<Object, Object>, Object[]>(hm.entrySet().iterator()) {
                    @Override
                    public Object[] apply(Entry<Object, Object> value) {
                        if(wrapParams) {
                            return new Object[] { value.getKey(), value.getValue() };
                        }
                        return (Object[]) value.getValue();
                    }
                };
            } else {
                return new IteratorMap<Object, Object[]>(new ArrayIterator(is)) {
                    @Override
                    public Object[] apply(Object value) {
                        if(wrapParams) {
                            return new Object[] { value };
                        }
                        return (Object[]) value;
                    }
                };
            }
        } catch (Exception ex) {
            Logger.getLogger(JSONDataProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Collections.EMPTY_LIST.iterator();
    }

}