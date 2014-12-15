package com.orasi.utils.exception;

import org.openqa.selenium.WebDriver;

/**
 * 
 * @author SonHuy.Pham@Disney.com
 */
public class BoundsCheckFailedException extends AutomationException {

    private static final long serialVersionUID = 1L;
    WebDriver driver = null;
    
    public BoundsCheckFailedException() {
        super("No Availability");
    }
    
    public BoundsCheckFailedException(String message) {
        super("No Availability: " + message);
    }
    
    public BoundsCheckFailedException(String message, Throwable cause) {
        super("No Availability: " + message, cause);
    }
    
    public BoundsCheckFailedException(WebDriver driver) {
        super("No Availability: ", driver);
        this.driver = driver;
    }
    
    public BoundsCheckFailedException(String message, WebDriver driver) {
        super("No Availability: " + message);
        this.driver = driver;
    }
    
    public BoundsCheckFailedException(String message, Throwable cause, WebDriver driver) {
        super("No Availability: " + message, cause, driver);
        this.driver = driver;
    }
    
    public <T extends AutomationException> void assertEquals(int lhs, int rhs) throws Exception {
        super.assertEquals(driver, (lhs == rhs), this.getClass());
    }
    
    public <T extends AutomationException> void assertEquals(String lhs, String rhs) throws Exception {
        if(lhs == null || rhs == null) {
            super.assertEquals(driver, false, this.getClass());
        }
        super.assertEquals(driver, lhs.equals(rhs), this.getClass());
    }
    
    public <T extends AutomationException> void assertEquals(boolean value) throws Exception {
        super.assertEquals(driver, value, this.getClass());
    }
}
