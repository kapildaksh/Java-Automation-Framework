package com.orasi.core.angular;

import org.openqa.selenium.WebDriverException;

/*
 * Original Code from https://github.com/paul-hammant/ngWebDriver
 */

public class VariableNotInScopeException extends WebDriverException {
    /**
     * 
     */
    private static final long serialVersionUID = 6602127720663559390L;

    public VariableNotInScopeException(String msg) {
        super(msg);
    }
}
