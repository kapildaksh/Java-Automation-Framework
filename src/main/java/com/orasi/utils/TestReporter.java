package com.orasi.utils;
import java.sql.Timestamp;

import org.testng.Assert;
import org.testng.Reporter;

public class TestReporter {
    private static boolean printToConsole = false;
    
    private static String getTimestamp(){
	return new Timestamp(new java.util.Date().getTime()).toString() + " :: ";
    }
    public static void setPrintToConsole(boolean printToConsole){
	TestReporter.printToConsole = printToConsole;
    }
    
    public static boolean getPrintToConsole(){
	return printToConsole;
    }
    
    public static void logStep(String step) {
	Reporter.log("<br/><b><font size = 4>Step: " + step
		+ "</font></b><br/>");
	if(getPrintToConsole()) System.out.println(step);
    }

    /*
     * public static void logScenario(){
     * Reporter.log("<br/><b><font size = 4>Data Scenario: " +
     * Datatable.getCurrentScenario()+ "</font></b><br/>"); }
     */

    public static void logScenario(String scenario) {
	Reporter.log("<br/><b><font size = 4>Data Scenario: " + scenario
		+ "</font></b><br/>");
	if(getPrintToConsole()) System.out.println(getTimestamp() + scenario);
    }

    public static void debugLog(String message) {
	Reporter.log(getTimestamp() + message + "<br />");
	if(getPrintToConsole()) System.out.println(getTimestamp() + message.trim());
    }

    public static void log(String message) {
	Reporter.log(getTimestamp() + " <i><b>" + message + "</b></i><br />");
	if(getPrintToConsole()) System.out.println(getTimestamp() + message);
    }

    public static void assertTrue(boolean condition, String description) {	
	try{
	    Assert.assertTrue(condition, description);
	}catch (AssertionError failure){
	    Reporter.log(getTimestamp() + " <font size = 2 color=\"red\"><b><u>Assert True- " + description + "</font></u></b><br />");
	    if(getPrintToConsole()) System.out.println(getTimestamp() + description);
	    Assert.fail(description);
	}
	Reporter.log(getTimestamp() + " <font size = 2 color=\"green\"><b><u>Assert True- " + description + "</font></u></b><br />");
	if(getPrintToConsole()) System.out.println(getTimestamp() + description);
    }
    
    public static void assertFalse(boolean condition, String description) {	
   	try{
   	    Assert.assertFalse(condition, description);
   	}catch (AssertionError failure){
   	    Reporter.log(getTimestamp() + " <font size = 2 color=\"red\"><b><u>Assert False- " + description + "</font></u></b><br />");
   	 if(getPrintToConsole()) System.out.println(getTimestamp() + description);
   	    Assert.fail(description);
   	}
   	Reporter.log(getTimestamp() + " <font size = 2 color=\"green\"><b><u>Assert False- " + description + "</font></u></b><br />");
   	if(getPrintToConsole()) System.out.println(getTimestamp() + description);
       }
    
    public static void assertEquals(boolean condition, String description) {
   	try{
   	    Assert.assertEquals(condition, description);
   	}catch (AssertionError failure){
   	    Reporter.log(getTimestamp() + " <font size = 2 color=\"red\"><b><u>Assert Equals- " + description + "</font></u></b><br />");
   	 if(getPrintToConsole()) System.out.println(getTimestamp() + description);
   	    Assert.fail(description);
   	}
   	Reporter.log(getTimestamp() + " <font size = 2 color=\"green\"><b><u>Assert Equals- " + description + "</font></u></b><br />");
   	if(getPrintToConsole()) System.out.println(getTimestamp() + description);
       }
    
    public static void assertNotEquals(boolean condition, String description) {
   	try{
   	    Assert.assertNotEquals(condition, description);
   	}catch (AssertionError failure){
   	    Reporter.log(getTimestamp() + " <font size = 2 color=\"red\"><b><u>Assert Not Equals- " + description + "</font></u></b><br />");
   	 if(getPrintToConsole()) System.out.println(getTimestamp() + description);
   	    Assert.fail(description);
   	}
   	Reporter.log(getTimestamp() + " <font size = 2 color=\"green\"><b><u>Assert Not Equals- " + description + "</font></u></b><br />");
   	if(getPrintToConsole()) System.out.println(getTimestamp() + description);
       }
    
    public static void assertGreaterThanZero(int value) {
   	try{
   	    Assert.assertTrue(value > 0);
   	}catch (AssertionError failure){
   	    Reporter.log(getTimestamp() + " <font size = 2 color=\"red\"><b><u>Assert Greater Than Zero- Assert " + value + " is greater than zero</font></u></b><br />");
   	 if(getPrintToConsole()) System.out.println(getTimestamp() + "Assert Greater Than Zero- Assert " + value + " is greater than zero");
   	    Assert.fail("Assert " + value + " is greater than zero");
   	}
   	Reporter.log(getTimestamp() + " <font size = 2 color=\"green\"><b><u>Assert Greater Than Zero- Assert " + value + " is greater than zero</font></u></b><br />");
   	if(getPrintToConsole()) System.out.println(getTimestamp() + "Assert Greater Than Zero- Assert " + value + " is greater than zero");
       }
    
    public static void assertGreaterThanZero(float value) {
   	try{
   	    Assert.assertTrue(value > 0);
   	}catch (AssertionError failure){
   	    Reporter.log(getTimestamp() + " <font size = 2 color=\"red\"><b><u>Assert Greater Than Zero- Assert " + value + " is greater than zero</font></u></b><br />");
   	 if(getPrintToConsole()) System.out.println(getTimestamp() + "Assert Greater Than Zero- Assert " + value + " is greater than zero");
   	    Assert.fail("Assert " + value + " is greater than zero");
   	}
   	Reporter.log(getTimestamp() + " <font size = 2 color=\"green\"><b><u>Assert Greater Than Zero- Assert " + value + " is greater than zero</font></u></b><br />");
   	if(getPrintToConsole()) System.out.println(getTimestamp() + "Assert Greater Than Zero- Assert " + value + " is greater than zero");
       }
    
    public static void assertGreaterThanZero(double value) {
   	try{
   	    Assert.assertTrue(value > 0);
   	}catch (AssertionError failure){
   	    Reporter.log(getTimestamp() + " <font size = 2 color=\"red\"><b><u>Assert Greater Than Zero- Assert " + value + " is greater than zero</font></u></b><br />");
   	 if(getPrintToConsole()) System.out.println(getTimestamp() + "Assert Greater Than Zero- Assert " + value + " is greater than zero");
   	    Assert.fail("Assert " + value + " is greater than zero");
   	}
   	Reporter.log(getTimestamp() + " <font size = 2 color=\"green\"><b><u>Assert Greater Than Zero- Assert " + value + " is greater than zero</font></u></b><br />");
   	if(getPrintToConsole()) System.out.println(getTimestamp() + "Assert Greater Than Zero- Assert " + value + " is greater than zero");
       }
    
    public static void assertNull(boolean condition, String description) {
   	try{
   	    Assert.assertNull(condition, description);
   	}catch (AssertionError failure){
   	    Reporter.log(getTimestamp() + " <font size = 2 color=\"red\"><b><u>Assert Null- " + description + "</font></u></b><br />");
   	 if(getPrintToConsole()) System.out.println(getTimestamp() + description);
   	    Assert.fail(description);
   	}
   	Reporter.log(getTimestamp() + " <font size = 2 color=\"green\"><b><u>Assert Null- " + description + "</font></u></b><br />");
   	if(getPrintToConsole()) System.out.println(getTimestamp() + description);
       }
    
    public static void assertNotNull(boolean condition, String description) {
   	try{
   	    Assert.assertNotNull(condition, description);
   	}catch (AssertionError failure){
   	    Reporter.log(getTimestamp() + " <font size = 2 color=\"red\"><b><u>Assert Not Null- " + description + "</font></u></b><br />");
   	 if(getPrintToConsole()) System.out.println(getTimestamp() + description);
   	    Assert.fail(description);
   	}
   	Reporter.log(getTimestamp() + "<font size = 2 color=\"green\"><b><u>Assert Not Null- " + description + "</font></u></b><br />");
   	if(getPrintToConsole()) System.out.println(getTimestamp() + description);
       }
}
