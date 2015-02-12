package com.orasi.utils.rest.blueprint;

/**
 * Data Warehouse for files in the API Blueprints (parsed by the snowcrash
 * binary) format. These are Markdown-formatted testing documents which
 * provide basically a full API that can be introspected.
 * 
 * @author Brian Becker
 */
import com.orasi.utils.rest.RestCollection;
import java.util.HashMap;
import java.util.Map;

/**
 * The SnowcrashWarehouse allows for the retrieval of a number of API Blueprint
 * Collections as well as other data for using these APIs.
 * 
 * @author Brian Becker
 */
public class SnowcrashWarehouse {
    
    private final String directory;
    private final String url;
    
    public SnowcrashWarehouse(String directory, String url) {
        this.directory = directory;
        this.url = url;
    }
    
    public static SnowcrashWarehouse dir(String directory, String endpoint) {
        return new SnowcrashWarehouse(directory, endpoint);
    }
    
    public RestCollection collection(String name) {
        return SnowcrashCollection.file(getClass().getResource(directory + name + ".md"), url);
    }
    
    public Map environment(String name) {
        return new HashMap();
    }
}

