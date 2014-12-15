package com.orasi.utils.exception;

import org.openqa.selenium.WebDriver;

/**
 * 
 * @author SonHuy.Pham@Disney.com 
 */
public class TermsOfUseUpdateException extends AutomationException {
    
    private static final long serialVersionUID = 1L;

    public TermsOfUseUpdateException() {
        super("Disney account requires TOU update");
    }
    
    public TermsOfUseUpdateException(String message) {
        super("Disney account requires TOU update: " + message);
    }
    
    public TermsOfUseUpdateException(String message, Throwable cause) {
        super("Disney account requires TOU update: " + message, cause);
    }
    
    public TermsOfUseUpdateException(WebDriver driver) {
        super("Disney account requires TOU update: ", driver);
    }
    
    public TermsOfUseUpdateException(WebDriver driver, Throwable cause) {
        super("Disney account requires TOU update: ", cause, driver);
    }
    
    public TermsOfUseUpdateException(String message, WebDriver driver) {
        super("Disney account requires TOU update: " + message, driver);
    }
    
    public TermsOfUseUpdateException(String message, Throwable cause, WebDriver driver) {
        super("Disney account requires TOU update: " + message, cause, driver);
    }
}
