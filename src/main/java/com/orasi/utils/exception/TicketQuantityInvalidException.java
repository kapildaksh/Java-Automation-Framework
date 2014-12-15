package com.orasi.utils.exception;

import org.openqa.selenium.WebDriver;

/**
 * 
 * @author SonHuy.Pham@Disney.com
 */
public class TicketQuantityInvalidException extends AutomationException {

    private static final long serialVersionUID = 1L;

    public TicketQuantityInvalidException() {
        super("Ticket Quantity Is Invalid");
    }
    
    public TicketQuantityInvalidException(String message) {
        super("Ticket Quantity Is Invalid: " + message);
    }
    
    public TicketQuantityInvalidException(String message, Throwable cause) {
        super("Ticket Quantity Is Invalid: " + message, cause);
    }
    
    public TicketQuantityInvalidException(WebDriver driver) {
        super("Ticket Quantity Is Invalid: ", driver);
    }
    
    public TicketQuantityInvalidException(String message, WebDriver driver) {
        super("Ticket Quantity Is Invalid: " + message);
    }
    
    public TicketQuantityInvalidException(String message, Throwable cause, WebDriver driver) {
        super("Ticket Quantity Is Invalid: " + message, cause, driver);
    }
}
