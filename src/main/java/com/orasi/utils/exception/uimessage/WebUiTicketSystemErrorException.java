package com.orasi.utils.exception.uimessage;

import org.openqa.selenium.WebDriver;

/**
 * 
 * @author SonHuy.Pham@Disney.com 
 */
public class WebUiTicketSystemErrorException extends WebUiErrorException {
    
    private static final long serialVersionUID = 1L;

    public WebUiTicketSystemErrorException() {
        super("Ticket-system/Maintenance Exception");
    }
    
    public WebUiTicketSystemErrorException(String message) {
        super("Ticket-system/Maintenance Exception: " + message);
    }
    
    public WebUiTicketSystemErrorException(String message, Throwable cause) {
        super("Ticket-system/Maintenance Exception: " + message, cause);
    }
    
    public WebUiTicketSystemErrorException(WebDriver driver) {
        super("Ticket-system/Maintenance Exception: ", driver);
    }
    
    public WebUiTicketSystemErrorException(String message, WebDriver driver) {
        super("Ticket-system/Maintenance Exception: " + message, driver);
    }
    
    public WebUiTicketSystemErrorException(String message, Throwable cause, WebDriver driver) {
        super("Ticket-system/Maintenance Exception: " + message, cause, driver);
    }
}
