package com.orasi.rest.misc;

import com.orasi.utils.test.cukes.CucumberSuite;
import cucumber.api.CucumberOptions;

/**
 * This is another test of the Microblog with Example Warehouse.
 * 
 * @author Brian Becker
 */
@CucumberOptions(plugin = {"com.orasi.utils.test.cukes.ConsoleFormatter"})
public class MicroblogExampleCucumberTest extends CucumberSuite {
    public MicroblogExampleCucumberTest() throws ClassNotFoundException {
        super(MicroblogExampleCucumberTest.class);
    }
}