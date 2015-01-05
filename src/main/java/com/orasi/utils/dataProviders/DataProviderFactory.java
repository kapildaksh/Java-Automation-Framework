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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author brian.becker
 */
public class DataProviderFactory {
    
    /**
     * Get a factory to create JDBC SQLite Data Providers.
     * 
     * @return      Factory which constructs SQLite providers.
     */
    public static JDBCDataProviderFactory getSqliteFactory() {
        return new JDBCDataProviderFactory("org.sqlite.JDBC", "sqlite");
    }
    
    /**
     * Get a factory to create JDBC Excel Data Providers.
     * 
     * @return      Factory which constructs Excel providers.
     */
    public static JDBCDataProviderFactory getExcelFactory() {
        return new JDBCDataProviderFactory("com.googlecode.sqlsheet.Driver", "xls:file");
    }
    
    /**
     * Get a factory to create Jackson Json Data Providers.
     * 
     * @return      Factory which constructs JSON providers.
     */
    public static JacksonDataProviderFactory getJsonFactory() {
        return new JacksonDataProviderFactory(new ObjectMapper());
    }
    
    /**
     * Get a factory to create Jackson XML Data Providers.
     * 
     * @return      Factory which constructs XML providers.
     */
    public static JacksonDataProviderFactory getXmlFactory() {
        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        XmlMapper mapper = new XmlMapper(module);
        return new JacksonDataProviderFactory(mapper);
    }
    
    /**
     * Get a factory to create YAML Data Providers.
     * 
     * @return      Factory which constructs YAML providers.
     */
    public static JacksonDataProviderFactory getYamlFactory() {
        return new JacksonDataProviderFactory(new ObjectMapper(new YAMLFactory()));
    }
    
    /**
     * Get a factory to create Jackson CSV Data Providers.
     * 
     * @return      Factory which constructs CSV providers
     */
    public static JacksonDataProviderFactory getCsvFactory() {
        CsvMapper mapper = new CsvMapper();
        mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
        return new JacksonDataProviderFactory(mapper);
    }
    
}
