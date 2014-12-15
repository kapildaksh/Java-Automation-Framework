package com.orasi.utils.exception;

import org.openqa.selenium.WebDriver;

/**
 * 
 * @author SonHuy.Pham@Disney.com
 */
public class NoResortRoomCarouselException extends AutomationException {

    private static final long serialVersionUID = 1L;

    public NoResortRoomCarouselException() {
        super("Unable to find resort room carousel");
    }
    
    public NoResortRoomCarouselException(String message) {
        super("Unable to find resort room carousel: " + message);
    }
    
    public NoResortRoomCarouselException(String message, Throwable cause) {
        super("Unable to find resort room carousel: " + message, cause);
    }
    
    public NoResortRoomCarouselException(WebDriver driver) {
        super("Unable to find resort room carousel: ", driver);
    }
    
    public NoResortRoomCarouselException(String message, WebDriver driver) {
        super("Unable to find resort room carousel: " + message, driver);
    }
    
    public NoResortRoomCarouselException(String message, Throwable cause, WebDriver driver) {
        super("Unable to find resort room carousel: " + message, cause, driver);
    }
}
