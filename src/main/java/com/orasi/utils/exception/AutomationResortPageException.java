package com.orasi.utils.exception;

import org.openqa.selenium.WebDriver;

/**
 * 
 * @author SonHuy.Pham@Disney.com
 */
public class AutomationResortPageException extends AutomationException {

    private static final long serialVersionUID = 1L;

    public AutomationResortPageException() {
        super("No Availability");
    }
    
    public AutomationResortPageException(String message) {
        super("No Availability: " + message);
    }
    
    public AutomationResortPageException(String message, Throwable cause) {
        super("No Availability: " + message, cause);
    }
    
    public AutomationResortPageException(WebDriver driver) {
        super("No Availability: ", driver);
    }
    
    public AutomationResortPageException(String message, WebDriver driver) {
        super("No Availability: " + message);
    }
    
    public AutomationResortPageException(String message, Throwable cause, WebDriver driver) {
        super("No Availability: " + message, cause, driver);
    }
}
