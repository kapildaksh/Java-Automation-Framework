package com.orasi.utils.exception.uimessage;

import org.openqa.selenium.WebDriver;

/**
 * 
 * @author SonHuy.Pham@Disney.com 
 */
public class WebUiMissingProfileException extends WebUiErrorException {
    
    private static final long serialVersionUID = 1L;

    public WebUiMissingProfileException() {
        super("Missing profile");
    }
    
    public WebUiMissingProfileException(String message) {
        super("Missing profile: " + message);
    }
    
    public WebUiMissingProfileException(String message, Throwable cause) {
        super("Missing profile: " + message, cause);
    }
    
    public WebUiMissingProfileException(WebDriver driver) {
        super("Missing profile: ", driver);
    }
    
    public WebUiMissingProfileException(String message, WebDriver driver) {
        super("Missing profile: " + message, driver);
    }
    
    public WebUiMissingProfileException(String message, Throwable cause, WebDriver driver) {
        super("Missing profile: " + message, cause, driver);
    }
}
