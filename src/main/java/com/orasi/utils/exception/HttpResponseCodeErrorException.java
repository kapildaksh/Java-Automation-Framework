package com.orasi.utils.exception;

import org.openqa.selenium.WebDriver;

/**
 * 
 * @author SonHuy.Pham@Disney.com 
 */
public class HttpResponseCodeErrorException extends AutomationException {
    
    private static final long serialVersionUID = 1L;

    public HttpResponseCodeErrorException() {
        super("Http Response Code Error");
    }
    
    public HttpResponseCodeErrorException(String message) {
        super("Http Response Code Error: " + message);
    }
    
    public HttpResponseCodeErrorException(String message, Throwable cause) {
        super("Http Response Code Error: " + message, cause);
    }
    
    public HttpResponseCodeErrorException(WebDriver driver) {
        super("Http Response Code Error: ", driver);
    }
    
    public HttpResponseCodeErrorException(String message, WebDriver driver) {
        super("Http Response Code Error: " + message, driver);
    }
    
    public HttpResponseCodeErrorException(String message, Throwable cause, WebDriver driver) {
        super("Http Response Code Error: " + message, cause, driver);
    }
}
