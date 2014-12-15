package com.orasi.utils.exception.uimessage;

import org.openqa.selenium.WebDriver;

/**
 * 
 * @author SonHuy.Pham@Disney.com 
 */
public class WebUiNoAvailabilityException extends WebUiErrorException {
    
    private static final long serialVersionUID = 1L;

    public WebUiNoAvailabilityException() {
        super("No Availability Exception");
    }
    
    public WebUiNoAvailabilityException(String message) {
        super("No Availability Exception: " + message);
    }
    
    public WebUiNoAvailabilityException(String message, Throwable cause) {
        super("No Availability Exception: " + message, cause);
    }
    
    public WebUiNoAvailabilityException(WebDriver driver) {
        super("No Availability Exception: ", driver);
    }
    
    public WebUiNoAvailabilityException(String message, WebDriver driver) {
        super("No Availability Exception: " + message, driver);
    }
    
    public WebUiNoAvailabilityException(String message, Throwable cause, WebDriver driver) {
        super("No Availability Exception: " + message, cause, driver);
    }
}
