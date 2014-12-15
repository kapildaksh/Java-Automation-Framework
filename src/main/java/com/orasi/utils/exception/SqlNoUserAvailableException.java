package com.orasi.utils.exception;

import org.openqa.selenium.WebDriver;

/**
 * 
 * @author SonHuy.Pham@Disney.com 
 */
public class SqlNoUserAvailableException extends AutomationException {
    
    private static final long serialVersionUID = 1L;

    public SqlNoUserAvailableException() {
        super("No Users Sql Database");
    }
    
    public SqlNoUserAvailableException(String message) {
        super("No Users Sql Database: " + message);
    }
    
    public SqlNoUserAvailableException(String message, Throwable cause) {
        super("No Users Sql Database: " + message, cause);
    }
    
    public SqlNoUserAvailableException(WebDriver driver) {
        super("No Users Sql Database: ", driver);
    }
    
    public SqlNoUserAvailableException(WebDriver driver, Throwable cause) {
        super("No Users Sql Database: ", cause, driver);
    }
    
    public SqlNoUserAvailableException(String message, WebDriver driver) {
        super("No Users Sql Database: " + message, driver);
    }
    
    public SqlNoUserAvailableException(String message, Throwable cause, WebDriver driver) {
        super("No Users Sql Database: " + message, cause, driver);
    }
}
