package com.orasi.utils.exception.uimessage;

import org.openqa.selenium.WebDriver;

/**
 * 
 * @author SonHuy.Pham@Disney.com 
 */
public class WebUiErrorTicketsException extends WebUiErrorException {
    
    private static final long serialVersionUID = 1L;

    public WebUiErrorTicketsException() {
        super("Tickets Error");
    }
    
    public WebUiErrorTicketsException(String message) {
        super("Tickets Error: " + message);
    }
    
    public WebUiErrorTicketsException(String message, Throwable cause) {
        super("Tickets Error: " + message, cause);
    }
    
    public WebUiErrorTicketsException(WebDriver driver) {
        super("Tickets Error: ", driver);
    }
    
    public WebUiErrorTicketsException(String message, WebDriver driver) {
        super("Tickets Error: " + message, driver);
    }
    
    public WebUiErrorTicketsException(String message, Throwable cause, WebDriver driver) {
        super("Tickets Error: " + message, cause, driver);
    }
}
