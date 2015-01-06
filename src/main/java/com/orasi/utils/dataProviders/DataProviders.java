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
 * Various static methods for constructing different types of data table
 * providers.
 * 
 * @version 1/06/2015
 * @author  Brian Becker
 */
public class DataProviders {
    
    /**
     * Initialize a SQL driver and catch exceptions.
     * 
     * @param       driverName 
     */
    private static void initDriver(String driverName) {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DataProviders.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Get a SQLite JDBC Data Provider
     * 
     * @version 1/06/2015
     * @author  Brian Becker
     * @param       filePath    File where database is located
     * @param       table       Table in database
     * @return      A SQLite provider
     */
    public static JDBCDataProvider createSqliteProvider(Path filePath, String table) {
        initDriver("org.sqlite.JDBC");
        return new JDBCDataProvider("jdbc:sqlite:" + filePath.toString(), table);
    }
    
    /**
     * Get an Excel JDBC Data Provider
     * 
     * @version 1/06/2015
     * @author  Brian Becker
     * @param       filePath    File where database is located
     * @param       table       Table in database
     * @return      An Excel provider
     */
    public static JDBCDataProvider createExcelProvider(Path filePath, String table) {
        initDriver("com.googlecode.sqlsheet.Driver");
        return new JDBCDataProvider("jdbc:xls:file:" + filePath.toString(), table);
    }
    
    /**
     * Get a MySQL JDBC Data Provider
     * 
     * @version 1/06/2015
     * @author  Brian Becker
     * @param       host        Host that database is located on
     * @param       port        Port which database is listening on
     * @param       db          Database name
     * @param       table       Table name
     * @param       user        User which has read access to table
     * @param       pass        Password for user
     * @return      A MySQL provider
     */
    public static JDBCDataProvider createMysqlProvider(String host, int port, String db, String table, String user, String pass) {
        initDriver("com.mysql.jdbc.Driver");
        return new JDBCDataProvider("jdbc:mysql://" + host + ":" + port + "/" + db + "?user=" + user + "&password=" + pass, table);
    }
    
    /**
     * Get an Oracle JDBC Data Provider
     * 
     * @version 1/06/2015
     * @author  Brian Becker
     * @param       host        Host that database is located on
     * @param       port        Port which database is listening on
     * @param       db          Database name
     * @param       table       Table name
     * @param       user        User which has read access to table
     * @param       pass        Password for user
     * @return      An Oracle provider
     */
    public static JDBCDataProvider createOracleProvider(String host, int port, String db, String table, String user, String pass) {
        initDriver("oracle.jdbc.OracleDriver");
        return new JDBCDataProvider("jdbc:oracle:thin:" + user + "/" + pass + "@" + host + ":" + port + ":" + db, table);
    }
    
    /**
     * Get a MSSQL JDBC Data Provider
     *
     * @version 1/06/2015
     * @author  Brian Becker
     * @param       host        Host that database is located on
     * @param       port        Port which database is listening on
     * @param       db          Database name
     * @param       table       Table name
     * @param       user        User which has read access to table
     * @param       pass        Password for user
     * @return      A MSSQL provider
     */
    public static JDBCDataProvider createMssqlProvider(String host, int port, String db, String table, String user, String pass) {
        initDriver("com.microsoft.jdbc.sqlserver.SQLServerDriver");
        return new JDBCDataProvider("jdbc:microsoft:sqlserver://" + host + ":" + port + ";databaseName=" + db + ";user=" + user + ";password=" + pass, table);
    }
    
    /**
     * Get a PostgreSQL JDBC Data Provider
     * 
     * @version 1/06/2015
     * @author  Brian Becker
     * @param       host        Host that database is located on
     * @param       port        Port which database is listening on
     * @param       db          Database name
     * @param       table       Table name
     * @param       user        User which has read access to table
     * @param       pass        Password for user
     * @return      A PostgreSQL provider
     */
    public static JDBCDataProvider createPostgresqlProvider(String host, int port, String db, String table, String user, String pass) {
        initDriver("org.postgresql.Driver");
        return new JDBCDataProvider("jdbc:postgresql://" + host + ":" + port + "/" + db + "?user=" + user + "&password=" + pass, table);
    }
    
    /**
     * Get an IBM DB2 JDBC Data Provider
     * 
     * @version 1/06/2015
     * @author  Brian Becker
     * @param       host        Host that database is located on
     * @param       port        Port which database is listening on
     * @param       db          Database name
     * @param       table       Table name
     * @param       user        User which has read access to table
     * @param       pass        Password for user
     * @return      An IBM DB2 provider
     */
    public static JDBCDataProvider createDB2Provider(String host, int port, String db, String table, String user, String pass) {
        initDriver("COM.ibm.db2.jdbc.app.DB2Driver");
        return new JDBCDataProvider("jdbc:db2://" + host + ":" + port + "/"+ db + ":user=" + user + ";password=" + pass, table);
    }

       
    /**
     * Get a factory to create Jackson Json Data Providers.
     * 
     * @version 1/06/2015
     * @author  Brian Becker
     * @return      Factory which constructs JSON providers.
     */
    public static JacksonDataProviderFactory createJsonFactory() {
        return new JacksonDataProviderFactory(new ObjectMapper());
    }
    
    /**
     * Get a factory to create Jackson XML Data Providers.
     * 
     * @version 1/06/2015
     * @author  Brian Becker
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
     * @version 1/06/2015
     * @author  Brian Becker
     * @return      Factory which constructs YAML providers.
     */
    public static JacksonDataProviderFactory createYamlFactory() {
        return new JacksonDataProviderFactory(new ObjectMapper(new YAMLFactory()));
    }
    
    /**
     * Get a factory to create Jackson CSV Data Providers.
     * 
     * @version 1/06/2015
     * @author  Brian Becker
     * @return      Factory which constructs CSV providers
     */
    public static JacksonDataProviderFactory createCsvFactory() {
        CsvMapper mapper = new CsvMapper();
        mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
        return new JacksonDataProviderFactory(mapper);
    }
    
}
