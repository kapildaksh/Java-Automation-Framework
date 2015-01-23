package com.orasi.rest.misc;

import com.orasi.arven.sandbox.rest.MockMicroblogServer;
import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * This is another test of the Microblog with Example Warehouse.
 * 
 * @author Brian Becker
 */
@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"})
public class MicroblogExampleCucumberTest {

    public void runCukes() {
        
    }

}
