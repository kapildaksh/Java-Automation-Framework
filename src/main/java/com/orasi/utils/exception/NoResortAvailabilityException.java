package com.orasi.utils.exception;

import org.openqa.selenium.WebDriver;

/**
 * 
 * @author SonHuy.Pham@Disney.com
 */
public class NoResortAvailabilityException extends NoAvailabilityException {

    private static final long serialVersionUID = 1L;

    public NoResortAvailabilityException() {
        super("Resort not available");
    }
    
    public NoResortAvailabilityException(String message) {
        super("Resort not available: " + message);
    }
    
    public NoResortAvailabilityException(String message, Throwable cause) {
        super("Resort not available: " + message, cause);
    }
    
    public NoResortAvailabilityException(WebDriver driver) {
        super("Resort not available: ", driver);
    }
    
    public NoResortAvailabilityException(String message, WebDriver driver) {
        super("Resort not available: " + message, driver);
    }
    
    public NoResortAvailabilityException(String message, Throwable cause, WebDriver driver) {
        super("Resort not available: " + message, cause, driver);
    }
}
