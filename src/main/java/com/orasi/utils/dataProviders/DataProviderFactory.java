/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.dataProviders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.nio.file.Path;

/**
 *
 * @author brian.becker
 */
public class DataProviderFactory {
    
    /**
     * Get a factory to create JDBC Data Providers.
     * 
     * @return      Factory which constructs JDBC providers.
     */
    public static JDBCDataProviderFactory getJdbcFactory() {
        return new JDBCDataProviderFactory();
    }
    
    /**
     * Get a factory to create Jackson Json Data Providers.
     * 
     * @param       filePath    Path to data file
     * @return      Factory which constructs JSON providers.
     */
    public static JacksonDataProviderFactory getJsonFactory(Path filePath) {
        return new JacksonDataProviderFactory(filePath, new ObjectMapper());
    }
    
    /**
     * Get a factory to create Jackson XML Data Providers.
     * 
     * @param       filePath    Path to data file
     * @return      Factory which constructs XML providers.
     */
    public static JacksonDataProviderFactory getXmlFactory(Path filePath) {
        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        XmlMapper mapper = new XmlMapper(module);
        return new JacksonDataProviderFactory(filePath, mapper);
    }
    
    /**
     * Get a factory to create YAML Data Providers.
     * 
     * @param       filePath    Path to data file
     * @return      Factory which constructs YAML providers.
     */
    public static JacksonDataProviderFactory getYamlFactory(Path filePath) {
        return new JacksonDataProviderFactory(filePath, new ObjectMapper(new YAMLFactory()));
    }
    
    /**
     * Get a factory to create Jackson CSV Data Providers.
     * 
     * @param       filePath    Path to data file
     * @return      Factory which constructs CSV providers
     */
    public static JacksonDataProviderFactory getCsvFactory(Path filePath) {
        CsvMapper mapper = new CsvMapper();
        mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
        return new JacksonDataProviderFactory(filePath, mapper);
    }
    
}
