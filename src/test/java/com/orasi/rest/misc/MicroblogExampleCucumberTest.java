package com.orasi.rest.misc;

import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;

/**
 * This is another test of the Microblog with Example Warehouse.
 * 
 * @author Brian Becker
 */
@CucumberOptions(plugin = {"com.orasi.utils.test.format.ConsoleFormatter"})
public class MicroblogExampleCucumberTest extends AbstractTestNGCucumberTests {
}
