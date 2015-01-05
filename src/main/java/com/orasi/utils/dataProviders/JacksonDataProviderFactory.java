/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.dataProviders;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.nio.file.Path;
import java.util.HashMap;

/**
 * The Jackson Data Provider factory provides a convenient way of setting
 * up one of six "structures" for instances, with a variety of different
 * document formats.
 * 
 * The structures are as follows:
 *      Root is an array, instances are parameters to function (in an array)
 *      Root is an array, instances are a traversable JsonNode
 *      Root is an array, instances are custom classes
 *      Root is a hash, instances are parameters to function (in an array)
 *      Root is a hash, instances are a traversable JsonNode
 *      Root is a hash, instances are custom classes
 * 
 * To get the benefits of structured data, you simply define a structure in
 * Java which will be automatically filled in.
 * 
 * @author Brian Becker
 */
public class JacksonDataProviderFactory {
   
    private final ObjectMapper map;

    /**
     * The Jackson Data Provider factory can be used to construct any type
     * of provider that Jackson natively supports. Helper constructors are
     * provided for JSON, XML, YAML, and CSV.
     * 
     * @param map       Jackson object-mapper
     */
    public JacksonDataProviderFactory(ObjectMapper map) {
        this.map = map;
    }

    /**
     * This is an array with params test case entries. The entries must be
     * of an array type, but the array elements themselves may be any
     * primitive type.
     * 
     * @param   filePath      Path of structured data
     * @return
     * @throws Throwable 
     */
    public JacksonDataProvider createArrayParams(Path filePath) throws Throwable {
        JavaType dt = this.map.getTypeFactory().constructArrayType(this.map.getTypeFactory().constructArrayType(Object.class));
        return new JacksonDataProvider(filePath, this.map, dt, false);
    }

    /**
     * This is an array with node test case entries. You may use special
     * functions such as .path("elementName") to traverse and retrieve values.
     * See the Jackson API for more details regarding the JsonNode Class.
     * 
     * @param   filePath      Path of structured data
     * @return
     * @throws Throwable 
     */
    public JacksonDataProvider createArrayNode(Path filePath) throws Throwable {
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
     * @param   filePath      Path of structured data
     * @param   structure     Structure of JSON instance entries
     * @return
     * @throws  Throwable 
     */
    public JacksonDataProvider createArrayStructured(Path filePath, Class structure) throws Throwable {
        map.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JavaType dt = this.map.getTypeFactory().constructArrayType(structure);
        return new JacksonDataProvider(filePath, this.map, dt, true);
    }

    /**
     * This is a hash table with params test case entries. The entries must be
     * of an array type, but the array elements themselves may be any
     * primitive type.
     *
     * @param   filePath      Path of structured data
     * @return
     * @throws  Throwable 
     */
    public JacksonDataProvider createHashParams(Path filePath) throws Throwable {
        JavaType dt = this.map.getTypeFactory().constructMapType(HashMap.class, this.map.getTypeFactory().constructType(String.class), this.map.getTypeFactory().constructArrayType(Object.class));
        return new JacksonDataProvider(filePath, this.map, dt, false);
    }

    /**
     * This is a hash table with node test case entries. You may use special
     * functions such as .path("elementName") to traverse and retrieve values.
     * See the Jackson API for more details regarding the JsonNode Class.
     *
     * @param   filePath      Path of structured data
     * @return
     * @throws  Throwable 
     */
    public JacksonDataProvider createHashNode(Path filePath) throws Throwable {
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
     * @param   filePath      Path of structured data
     * @param   structure     Structure of instance entries
     * @return
     * @throws  Throwable 
     */
    public JacksonDataProvider createHashStructured(Path filePath, Class structure) throws Throwable {
        this.map.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        JavaType dt = this.map.getTypeFactory().constructMapType(HashMap.class, String.class, structure);
        return new JacksonDataProvider(filePath, this.map, dt, true);        
    }
    
}
