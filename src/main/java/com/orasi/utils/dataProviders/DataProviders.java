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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author brian.becker
 */
public class DataProviders {
    
    /**
     * Initialize a SQL driver and catch exceptions.
     * 
     * @param driverName 
     */
    private static void initDriver(String driverName) {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(JDBCDataProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Get a factory to create JDBC SQLite Data Providers.
     * 
     * @param filePath
     * @param table
     * @return      Factory which constructs SQLite providers.
     */
    public static JDBCDataProvider createSqlite(Path filePath, String table) {
        return new JDBCDataProvider("jdbc:sqlite:" + filePath.toString(), table, null, null);
    }
    
    /**
     * Get a factory to create JDBC Excel Data Providers.
     * 
     * @param filePath
     * @param table
     * @return      Factory which constructs Excel providers.
     */
    public static JDBCDataProvider createExcel(Path filePath, String table) {
        initDriver("com.googlecode.sqlsheet.Driver");
        return new JDBCDataProvider("jdbc:xls:file:" + filePath.toString(), table, null, null);
    }
    
    /**
     * Get a factory to create JDBC MySQL Data Providers.
     * 
     * @param host
     * @param db
     * @param table
     * @param user
     * @param pass
     * @return      Factory which constructs MySQL providers.
     */
    public static JDBCDataProvider createMysql(String host, String db, String table, String user, String pass) {
        return new JDBCDataProvider("jdbc:mysql://" + host + "/" + db, table, user, pass);
    }
       
    /**
     * Get a factory to create Jackson Json Data Providers.
     * 
     * @return      Factory which constructs JSON providers.
     */
    public static JacksonDataProviderFactory createJsonFactory() {
        return new JacksonDataProviderFactory(new ObjectMapper());
    }
    
    /**
     * Get a factory to create Jackson XML Data Providers.
     * 
     * @return      Factory which constructs XML providers.
     */
    public static JacksonDataProviderFactory createXmlFactory() {
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
    public static JacksonDataProviderFactory createYamlFactory() {
        return new JacksonDataProviderFactory(new ObjectMapper(new YAMLFactory()));
    }
    
    /**
     * Get a factory to create Jackson CSV Data Providers.
     * 
     * @return      Factory which constructs CSV providers
     */
    public static JacksonDataProviderFactory createCsvFactory() {
        CsvMapper mapper = new CsvMapper();
        mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
        return new JacksonDataProviderFactory(mapper);
    }
    
}
