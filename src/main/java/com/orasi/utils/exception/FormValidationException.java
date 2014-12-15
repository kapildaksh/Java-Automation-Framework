package com.orasi.utils.exception;

import org.openqa.selenium.WebDriver;

/**
 * 
 * @author SonHuy.Pham@Disney.com
 */
public class FormValidationException extends AutomationException {
    
    private static final long serialVersionUID = 1L;

    public FormValidationException() {
        super("Detected a form validation error message");
    }
    
    public FormValidationException(String message) {
        super("Detected a form validation error message: " + message);
    }
    
    public FormValidationException(String message, Throwable cause) {
        super("Detected a form validation error message: " + message, cause);
    }
    
    public FormValidationException(WebDriver driver) {
        super("Detected a form validation error message: ", driver);
    }
    
    public FormValidationException(String message, WebDriver driver) {
        super("Detected a form validation error message: " + message);
    }
    
    public FormValidationException(String message, Throwable cause, WebDriver driver) {
        super("Detected a form validation error message: " + message, cause, driver);
    }
}
