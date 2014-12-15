package com.orasi.utils;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import org.testng.Reporter;

public class TestReporter {
	public static void logStep(String step){
		Reporter.log("<br/><b><font size = 4>Step: " + step + "</font></b><br/>");
	}
	
	 public static void logScenario(){
		Reporter.log("<br/><b><font size = 4>Data Scenario: " + Datatable.getCurrentScenario()+ "</font></b><br/>");
	}
	 
	 public static void logScenario(String scenario){
		Reporter.log("<br/><b><font size = 4>Data Scenario: " + scenario + "</font></b><br/>");
		}
	 
	 public static void log(String message){
		 Reporter.log(new Timestamp(new java.util.Date().getTime()) + " :: "+ message + "<br />");			
	 }
}
