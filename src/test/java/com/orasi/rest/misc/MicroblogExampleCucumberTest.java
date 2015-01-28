package com.orasi.rest.misc;

import com.orasi.utils.rest.CucumberNG;
import cucumber.api.CucumberOptions;

/**
 * This is another test of the Microblog with Example Warehouse.
 * 
 * @author Brian Becker
 */
@CucumberOptions(plugin = {"com.orasi.utils.test.format.ConsoleFormatter"})
public class MicroblogExampleCucumberTest extends CucumberNG {
    public MicroblogExampleCucumberTest() throws ClassNotFoundException {
        super(MicroblogExampleCucumberTest.class);
    }
}