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
import java.text.MessageFormat;
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
    
    // {0} File
    public static final String SQLITE_URI = "jdbc:sqlite:{0}";
    public static final String H2_EMBEDDED_URI = "jdbc:h2:file:{0};IFEXISTS=TRUE";
    public static final String EXCEL_URI = "jdbc:xls:file:{0}";
    
    // {0} Host {1} Port {2} Database {3} User {4} Password
    public static final String MYSQL_URI = "jdbc:mysql://{0}:{1}/{2}?user={3}&password={4}";
    public static final String ORACLE_THIN_URI = "jdbc:oracle:thin:{3}/{4}@{0}:{1}:{2}";
    public static final String MSSQL_URI = "jdbc:microsoft:sqlserver://{0}:{1};databaseName={2};user={3};password={4}";
    public static final String POSTGRESQL_URI = "jdbc:postgresql://{0}:{1}/{2}?user={3}&password={4}";
    public static final String DB2_URI = "jdbc:db2://{0}:{1}/{2}:user={3};password={4}";
    public static final String H2_REMOTE_URI = "jdbc:h2:tcp://{0}:{1}/{2};USER={3};PASSWORD={4}";

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
        return new JDBCDataProvider(MessageFormat.format(SQLITE_URI, filePath.toString()), table);
    }
    
    /**
     * Get an Embedded H2 JDBC Data Provider
     * 
     * @version 1/06/2015
     * @author  Brian Becker
     * @param       filePath    File where database is located
     * @param       table       Table in database
     * @return      A SQLite provider
     */
    public static JDBCDataProvider createEmbeddedH2Provider(Path filePath, String table) {
        initDriver("org.sqlite.JDBC");
        return new JDBCDataProvider(MessageFormat.format(H2_EMBEDDED_URI, filePath.toString()), table);
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
        return new JDBCDataProvider(MessageFormat.format(EXCEL_URI, filePath.toString()), table);
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
        return new JDBCDataProvider(MessageFormat.format(MYSQL_URI, host, port, db, user, pass), table);
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
        return new JDBCDataProvider(MessageFormat.format(ORACLE_THIN_URI, host, port, db, user, pass), table);
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
        return new JDBCDataProvider(MessageFormat.format(MSSQL_URI, host, port, db, user, pass), table);
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
        return new JDBCDataProvider(MessageFormat.format(POSTGRESQL_URI, host, port, db, user, pass), table);
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
        return new JDBCDataProvider(MessageFormat.format(DB2_URI, host, port, db, user, pass), table);
    }
    
    /**
     * Get an Remote H2 JDBC Data Provider
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
    public static JDBCDataProvider createRemoteH2Provider(String host, int port, String db, String table, String user, String pass) {
        initDriver("org.h2.Driver");
        return new JDBCDataProvider(MessageFormat.format(H2_REMOTE_URI, host, port, db, user, pass), table);
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
