package com.orasi.reporting;

import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

public class HtmlReportListener  extends TestListenerAdapter{
	OrasiReporter htmlReport = new OrasiReporter();
	@Override
	public void onTestFailure(ITestResult testResult) {
	//ScreenShot(testResult);
		//System.out.println("Test " + testResult.getInstanceName() + " failed.");
		//System.out.println();
		htmlReport.ReportEvent("Fail", "Java Exception Occured","<stepStackTrace>"+ stackTraceToString(testResult.getThrowable())+"</stepStackTrace>", true);
		//htmlReport.ReportEvent("Fail", "Java Exception Occured","<a href=\"javascript:;\" onclick=\"stackTrace.style.display = ''\">Show Stack Trace</a><p id=\"stackTrace\" style=\"display: none\">"+ stackTraceToString(testResult.getThrowable())+"</p>", true);
		//htmlReport.ReportEvent("Stop", testResult.getName(), "Test Stopped with Error", false);
	}
	public String stackTraceToString(Throwable e){
		StringBuilder sb = new StringBuilder();
		for (StackTraceElement element : e.getStackTrace()){
			sb.append(element.toString());
			sb.append("/n ");
		}
		return sb.toString();
	}
}
