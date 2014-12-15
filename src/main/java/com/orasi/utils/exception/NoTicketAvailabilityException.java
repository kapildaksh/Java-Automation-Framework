package com.orasi.utils.exception;

import org.openqa.selenium.WebDriver;

/**
 * 
 * @author SonHuy.Pham@Disney.com
 */
public class NoTicketAvailabilityException extends NoAvailabilityException {

    private static final long serialVersionUID = 1L;

    public NoTicketAvailabilityException() {
        super("Ticket not available");
    }
    
    public NoTicketAvailabilityException(String message) {
        super("Ticket not available: " + message);
    }
    
    public NoTicketAvailabilityException(String message, Throwable cause) {
        super("Ticket not available: " + message, cause);
    }
    
    public NoTicketAvailabilityException(WebDriver driver) {
        super("Ticket not available: ", driver);
    }
    
    public NoTicketAvailabilityException(String message, WebDriver driver) {
        super("Ticket not available: " + message, driver);
    }
    
    public NoTicketAvailabilityException(String message, Throwable cause, WebDriver driver) {
        super("Ticket not available: " + message, cause, driver);
    }
}
