package com.orasi.utils.dataProviders;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.file.Path;
import java.util.ArrayList;
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
 * @version     1/06/2015
 * @author      Brian Becker
 */
public class JacksonDataProviderFactory {
   
    private final ObjectMapper map;

    /**
     * The Jackson Data Provider factory can be used to construct any type
     * of provider that Jackson natively supports. Helper constructors are
     * provided for JSON, XML, YAML, and CSV.
     * 
     * @version 1/06/2015
     * @author  Brian Becker
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
     * @version 1/06/2015
     * @author  Brian Becker
     * @param   filePath      Path of structured data
     * @return
     * @throws  Exception 
     */
    public DataProvider createArrayParams(Path filePath) throws Exception {
        return new JacksonDataProvider(filePath, this.map, this.map.getTypeFactory().constructCollectionType(ArrayList.class, Object[].class), false);
    }

    /**
     * This is an array with node test case entries. You may use special
     * functions such as .path("elementName") to traverse and retrieve values.
     * See the Jackson API for more details regarding the JsonNode Class.
     * 
     * @version 1/06/2015
     * @author  Brian Becker
     * @param   filePath      Path of structured data
     * @return
     * @throws  Exception 
     */
    public DataProvider createArrayNode(Path filePath) throws Exception {
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
     * @version 1/06/2015
     * @author  Brian Becker
     * @param   filePath      Path of structured data
     * @param   structure     Structure of JSON instance entries
     * @return
     * @throws  Exception 
     */
    public DataProvider createArrayStructured(Path filePath, Class structure) throws Exception {
        map.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return new JacksonDataProvider(filePath, this.map, this.map.getTypeFactory().constructCollectionType(ArrayList.class, structure), true);
    }

    /**
     * This is a hash table with params test case entries. The entries must be
     * of an array type, but the array elements themselves may be any
     * primitive type.
     *
     * @version 1/06/2015
     * @author  Brian Becker
     * @param   filePath      Path of structured data
     * @return
     * @throws  Exception 
     */
    public DataProvider createHashParams(Path filePath) throws Exception {
        return new JacksonDataProvider(filePath, this.map, this.map.getTypeFactory().constructMapType(HashMap.class, String.class, Object[].class), false);
    }

    /**
     * This is a hash table with node test case entries. You may use special
     * functions such as .path("elementName") to traverse and retrieve values.
     * See the Jackson API for more details regarding the JsonNode Class.
     *
     * @version 1/06/2015
     * @author  Brian Becker
     * @param   filePath      Path of structured data
     * @return
     * @throws  Exception 
     */
    public DataProvider createHashNode(Path filePath) throws Exception {
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
     * @version 1/06/2015
     * @author  Brian Becker
     * @param   filePath      Path of structured data
     * @param   structure     Structure of instance entries
     * @return
     * @throws  Exception 
     */
    public DataProvider createHashStructured(Path filePath, Class structure) throws Exception {
        this.map.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return new JacksonDataProvider(filePath, this.map, this.map.getTypeFactory().constructMapType(HashMap.class, String.class, structure), true);        
    }
    
}
