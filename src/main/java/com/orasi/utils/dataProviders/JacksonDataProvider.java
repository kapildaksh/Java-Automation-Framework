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
 * This is a structured data provider. Instances can be held in either a 2d
 * array or in an associative array. Instances themselves can either be 2d
 * arrays of "objects" which are passed as parameters to the test case, or
 * they may be structured data. Structured data can be accessed either through
 * the JsonNode object, or custom objects which you provide to this class.
 * 
 * NOTE: Where JSON or other raw data itself is the structure that should be
 * passed into a test case, you should escape it and use any data provider you
 * like. This is for passing object-like data to test cases.
 * 
 * @version     12/30/2014
 * @author      Brian Becker
 */
public class JacksonDataProvider implements DataProvider {
    
    // Keep an instance of the object mapper around for reading the data
    private final ObjectMapper map;
    // Data type which the provider is going to use to map raw data
    private final JavaType dataType;
    
    // Raw Data
    private final byte[] mapData;
    // Wrapping parameters for non-params based method calls
    private final boolean wrapParams;
    
    /**
     * This creates the Jackson Data provider with a given file path, instance
     * structure, as well as instance array or hash map.
     * 
     * @param       filePath                file path of the data file
     * @version     12/30/2014
     * @author      Brian Becker
     * @param       map                     object mapper used to create type
     * @param       dataType                data type of test
     * @param       wrapParams              parameter wrapping needed
     * @throws      java.lang.Throwable
     */
    public JacksonDataProvider(Path filePath, ObjectMapper map, JavaType dataType, boolean wrapParams) throws Throwable {
        this.mapData = Files.readAllBytes(filePath);
        this.map = map;
        this.dataType = dataType;
        this.wrapParams = wrapParams;
    }
    
    /**
     * This gets the test data from a data file, given the file is in an
     * appropriate format for the data provider configuration. The outer
     * level must either be an array or a hash map, because it must be
     * able to be iterated through.
     * 
     * Example of JacksonDataProvider configured to output parameters from a
     * JSON file with a root-level hash table
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
    @Override
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
            Logger.getLogger(JacksonDataProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Collections.EMPTY_LIST.iterator();
    }

}