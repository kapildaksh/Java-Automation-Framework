package com.orasi.utils.exception.uimessage;

import org.openqa.selenium.WebDriver;

/**
 * 
 * @author SonHuy.Pham@Disney.com 
 */
public class WebUiPartialServiceException extends WebUiErrorException {
    
    private static final long serialVersionUID = 1L;

    public WebUiPartialServiceException() {
        super("Partial Service Failure Exception");
    }
    
    public WebUiPartialServiceException(String message) {
        super("Partial Service Failure Exception: " + message);
    }
    
    public WebUiPartialServiceException(String message, Throwable cause) {
        super("Partial Service Failure Exception: " + message, cause);
    }
    
    public WebUiPartialServiceException(WebDriver driver) {
        super("Partial Service Failure Exception: ", driver);
    }
    
    public WebUiPartialServiceException(String message, WebDriver driver) {
        super("Partial Service Failure Exception: " + message, driver);
    }
    
    public WebUiPartialServiceException(String message, Throwable cause, WebDriver driver) {
        super("Partial Service Failure Exception: " + message, cause, driver);
    }
}
