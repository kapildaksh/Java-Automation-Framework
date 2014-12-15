package com.orasi.utils.exception.automation;

import org.openqa.selenium.WebDriver;

import com.orasi.utils.exception.AutomationException;

/**
 * 
 * @author SonHuy.Pham@Disney.com
 */
public class PageObjectCtorNotRecognizedException extends AutomationException {

    private static final long serialVersionUID = 1L;

    public PageObjectCtorNotRecognizedException() {
        super("Unable to instantiate class instance");
    }
    
    public PageObjectCtorNotRecognizedException(String message) {
        super("Unable to instantiate class instance: " + message);
    }
    
    public PageObjectCtorNotRecognizedException(String message, Throwable cause) {
        super("Unable to instantiate class instance: " + message, cause);
    }
    
    public PageObjectCtorNotRecognizedException(WebDriver driver) {
        super("Unable to instantiate class instance: ", driver);
    }
    
    public PageObjectCtorNotRecognizedException(String message, WebDriver driver) {
        super("Unable to instantiate class instance: " + message, driver);
    }
    
    public PageObjectCtorNotRecognizedException(String message, Throwable cause, WebDriver driver) {
        super("Unable to instantiate class instance: " + message, cause, driver);
    }
}
