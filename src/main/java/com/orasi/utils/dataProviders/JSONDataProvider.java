package com.orasi.utils.dataProviders;

import java.util.Collections;
import java.util.Iterator;
//import org.codehaus.jackson.JsonParser;

public class JSONDataProvider {
    
    // File path of JSON data
    private String filePath;
    
    /**
     * This creates the JSON Data provider with a given file path.
     * 
     * @param       filePath                file path of the JSON file
     * @version     12/30/2014
     * @author      Brian Becker
     */
    public JSONDataProvider(String filePath) {
        this.filePath = filePath;        
    }
    
    
    /**
     * This gets the test data from a csv file.  It returns all the data 
     * as a 2d array
     * 
     * @param	filePath		the file path of the CSV file
     * @version	12/30/2014
     * @author 	Brian Becker
     * @return 	Iterator of Object[]
     */
    public static Iterator<Object[]> getTestScenarioData(String filePath){
   
        Iterator<Object[]> o = Collections.emptyListIterator();
        return o;

    }

}
