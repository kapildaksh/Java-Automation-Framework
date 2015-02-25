package com.orasi.reporting;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.HasCapabilities;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

import com.orasi.utils.WebDriverSetup;

public class OrasiReporter {
	public WebDriver driver;
	public File htmlFile = new File("C:\\Temp\\Temp.html");
	public File file;
	public FileWriter reportFileWriter;
	public PrintWriter reportPrintWriter;
	public static int testCount;
	public static int testIteration;
	public static int stepCount;
	DateFormat fullDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	DateFormat simpleTime = new SimpleDateFormat("HH:mm:ss");
	
	public void ReportEvent(String eventCase,String action,String message, boolean screenshot){
		file = new File("C:\\Temp\\Temp"+testIteration+".xml");
		try {
			reportFileWriter = new FileWriter(file,true);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		driver = WebDriverSetup.getDriver();
		reportPrintWriter = new PrintWriter(reportFileWriter);
		stepCount++;
		switch (eventCase){
		case "Pass":
			reportPrintWriter.println("<testStep>");
			reportPrintWriter.println("<stepNo>"+stepCount+"</stepNo>");
			reportPrintWriter.println("<stepResult>Pass</stepResult>");
			reportPrintWriter.println("<stepTimestamp>"+simpleTime.format(Calendar.getInstance().getTime())+"</stepTimestamp>");
			reportPrintWriter.println("<stepName>"+action+"</stepName>");
			reportPrintWriter.println("<stepDesc>"+message+"</stepDesc>");
			if(screenshot){
				//TakeScreenshot
				File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			    // now save the screenshot to a file some place
			    try {
			    	String filePath = "c:\\Temp\\images\\"+System.currentTimeMillis()+".png";
			    	FileUtils.copyFile(scrFile, new File(filePath));
			    	reportPrintWriter.println("<stepScreenshot>"+filePath+"</stepScreenshot>");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			reportPrintWriter.println("</testStep>");
			reportPrintWriter.close();
			break;
		case "Fail":
			reportPrintWriter.println("<testStep>");
			reportPrintWriter.println("<stepNo>"+stepCount+"</stepNo>");
			reportPrintWriter.println("<stepResult>Failed</stepResult>");
			reportPrintWriter.println("<stepTimestamp>"+simpleTime.format(Calendar.getInstance().getTime())+"</stepTimestamp>");
			reportPrintWriter.println("<stepName>"+action+"</stepName>");
			reportPrintWriter.println("<stepDesc>"+message+"</stepDesc>");
			if(screenshot){
				//TakeScreenshot
				File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			    // now save the screenshot to a file some place
			    try {
			    	String filePath = "c:\\Temp\\images\\"+System.currentTimeMillis()+".png";
			    	FileUtils.copyFile(scrFile, new File(filePath));
			    	reportPrintWriter.println("<stepScreenshot>"+filePath+"</stepScreenshot>");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			reportPrintWriter.println("</testStep>");
			reportPrintWriter.close();
			break;
		case "Info":
			reportPrintWriter.println("<testStep>");
			reportPrintWriter.println("<stepNo>"+stepCount+"</stepNo>");
			reportPrintWriter.println("<stepResult>Info</stepResult>");
			reportPrintWriter.println("<stepTimestamp>"+simpleTime.format(Calendar.getInstance().getTime())+"</stepTimestamp>");
			reportPrintWriter.println("<stepName>"+action+"</stepName>");
			reportPrintWriter.println("<stepDesc>"+message+"</stepDesc>");
			if(screenshot){
				//TakeScreenshot
				File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			    // now save the screenshot to a file some place
			    try {
			    	String filePath = "c:\\Temp\\images\\"+System.currentTimeMillis()+".png";
			    	FileUtils.copyFile(scrFile, new File(filePath));
			    	reportPrintWriter.println("<stepScreenshot>"+filePath+"</stepScreenshot>");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			reportPrintWriter.println("</testStep>");
			reportPrintWriter.close();
			break;
		case "Warning":
			reportPrintWriter.println("<testStep>");
			reportPrintWriter.println("<stepNo>"+stepCount+"</stepNo>");
			reportPrintWriter.println("<stepResult>Warning</stepResult>");
			reportPrintWriter.println("<stepTimestamp>"+simpleTime.format(Calendar.getInstance().getTime())+"</stepTimestamp>");
			reportPrintWriter.println("<stepName>"+action+"</stepName>");
			reportPrintWriter.println("<stepDesc>"+message+"</stepDesc>");
			if(screenshot){
				//TakeScreenshot
				File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			    // now save the screenshot to a file some place
			    try {
			    	String filePath = "c:\\Temp\\images\\"+System.currentTimeMillis()+".png";
			    	FileUtils.copyFile(scrFile, new File(filePath));
			    	reportPrintWriter.println("<stepScreenshot>"+filePath+"</stepScreenshot>");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			reportPrintWriter.println("</testStep>");
			reportPrintWriter.close();
			break;
		case "Start":
			stepCount=0;
			testIteration++;
			file = new File("C:\\Temp\\Temp"+testIteration+".xml");
			if(file.exists()){
				reportPrintWriter.close();
				try {
					reportFileWriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				file.delete();
			}
				try {
					reportFileWriter = new FileWriter(file,true);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			reportPrintWriter = new PrintWriter(reportFileWriter);
			
			reportPrintWriter.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			reportPrintWriter.println("<testRun>");
			reportPrintWriter.println("<testName>"+message+"</testName>");
			reportPrintWriter.println("<testStartTime>"+fullDateFormat.format(Calendar.getInstance().getTime())+"</testStartTime>");
			Capabilities cap = ((RemoteWebDriver) driver).getCapabilities();
			reportPrintWriter.println("<testBrowser>"+ cap.getBrowserName() + " " + cap.getVersion() + "</testBrowser>");
			reportPrintWriter.println("<results>");
			reportPrintWriter.close();
			break;
		case "Stop":
			reportPrintWriter.println("</results>");
			reportPrintWriter.println("</testRun>");
			reportPrintWriter.close();
			stepCount = 0;
			//rename file
			String outputFileName = action;
			File file2 = new File("C:\\Temp\\"+testIteration+"_"+ outputFileName+".xml");
			if(file2.exists()){
				file2.delete();
			}
			file.renameTo(file2);
			//Reporter.setEscapeHtml(false);
			//Reporter.log("<a href="+file2.getAbsolutePath()+">"+file2.getAbsolutePath()+"</a>");
			break;
		}
	}
	public void GenerateHTML(String xmlFiles,String outputName){
		String[] XMLs = xmlFiles.split(","); 
		try {
			reportFileWriter = new FileWriter(htmlFile,true);
			reportPrintWriter = new PrintWriter(reportFileWriter);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(htmlFile.exists()){
			reportPrintWriter.close();
			try {
				reportFileWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			htmlFile.delete();
			try {
				reportFileWriter = new FileWriter(htmlFile,true);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			reportPrintWriter = new PrintWriter(reportFileWriter);
		}

		reportPrintWriter.println("<html>");
		reportPrintWriter.println("<body>");
		//Header
		reportPrintWriter.println("<head><center><h2>"+outputName+"</h2></center></head>");
		//Pie Chart
		//See: https://developers.google.com/chart/interactive/docs/gallery/piechart
		reportPrintWriter.println("<center><div id=\"piechart\" style=\"width: 500px; height: 300px;\"></div>");
		//Summary Variables
		reportPrintWriter.println("<table border=\"1\" style=\"width:25%\">");
		reportPrintWriter.println("<tr>");
		reportPrintWriter.println("<th><b>Total Passed</b></th>");
		reportPrintWriter.println("<th><b>Total Failed</b></th>");
		reportPrintWriter.println("<th><b>Total Warnings</b></th>");
		reportPrintWriter.println("</tr><tr>");
		reportPrintWriter.println("<td align=right id=\"totalPassed\">0</td>");
		reportPrintWriter.println("<td align=right id=\"totalFailed\">0</td>");
		reportPrintWriter.println("<td align=right id=\"totalWarnings\">0</td>");
		reportPrintWriter.println("</tr></table></center>");
		for(int i=0;i<XMLs.length;i++){
			try { 
				File xmlFile = new File("C:\\Temp\\"+XMLs[i]+".xml");
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(xmlFile);
			 
				//optional, but recommended
				//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
				doc.getDocumentElement().normalize();
				
				//Test Name
				Element testName = (Element) doc.getElementsByTagName("testName").item(0);
				Element testStartTime = (Element) doc.getElementsByTagName("testStartTime").item(0);
				Element testBrowser = (Element) doc.getElementsByTagName("testBrowser").item(0);
				reportPrintWriter.println("<center>");
				//Run Information
				reportPrintWriter.println("<h2>"+ testName.getTextContent() + ":"+testStartTime.getTextContent()+"</h2>");
				reportPrintWriter.println("<h3>"+ testBrowser.getTextContent() + "</h3>");
				//Summary Table
				reportPrintWriter.println("<table id=\"summaryTable"+i+"\" border=\"1\" style=\"width:75%\">");
				reportPrintWriter.println("<tr>");
				reportPrintWriter.println("<th>Results Summary</th>");
				reportPrintWriter.println("<th>Total Runtime</th>");
				reportPrintWriter.println("</tr>");
				reportPrintWriter.println("<tr>");
				reportPrintWriter.println("<td align=center>::Enable Javascript to View Field::</td>");
				reportPrintWriter.println("<td align=center>::Enable Javascript to View Field::</td>");
				reportPrintWriter.println("</tr>");
				reportPrintWriter.println("</table>");
				//Test Action Display Toggle
				reportPrintWriter.println("<button onclick=\"toggleObjectVisibility(document.getElementById('resultsTable"+i+"'))\">Show/Hide Test Steps</button>");
				reportPrintWriter.println("</center>");
				//CSS styles
				reportPrintWriter.println("<style type=\"text/css\">table{width:95%;margin-left:auto;margin-right:auto} "
						+ "tr:nth-child(even){background:#e4e4e4} "
						+ "tr:nth-child(odd){background:#FFFFFF} "
						+ "td{white-space: nowrap}</style>");
				//Test Actions table
				reportPrintWriter.println("<table id=\"resultsTable"+i+"\" border=\"1\" style=\"display:none\">");
				reportPrintWriter.println("<tr>");
				reportPrintWriter.println("<th>Step No.</th>");
				reportPrintWriter.println("<th>Status</th>");
				reportPrintWriter.println("<th>Time</th>");
				reportPrintWriter.println("<th>Test Step</th>");
				reportPrintWriter.println("<th>Step Description</th>");
				reportPrintWriter.println("</tr>");
			 	
				NodeList nList = doc.getElementsByTagName("testStep");
				
				for (int temp = 0; temp < nList.getLength(); temp++) {
			 
					Node nNode = nList.item(temp);
			 
					//System.out.println("\nCurrent Element :" + nNode.getNodeName());
			 
					Element eElement = (Element) nNode;
		 
					reportPrintWriter.println("<tr>");
					//Step No.
					reportPrintWriter.println("<td align=center>"+eElement.getElementsByTagName("stepNo").item(0).getTextContent()+"</td>");
					//Result
					switch(eElement.getElementsByTagName("stepResult").item(0).getTextContent()){
						case "Pass":
							reportPrintWriter.println("<td align=center bgcolor=\"#00FF00\">&#x2713;</td>");
							break;
						case "Failed":
							reportPrintWriter.println("<td align=center bgcolor=\"#FF0000\">&#x2717;</td>");
							break;
						case "Info":
							reportPrintWriter.println("<td align=center bgcolor=\"#FFFFFF\">&nbsp;</td>");
							break;
						case "Warning":
							reportPrintWriter.println("<td align=center bgcolor=\"#FFA500\">!</td>");
							break;
					}
					//Timestamp
					reportPrintWriter.println("<td>"+eElement.getElementsByTagName("stepTimestamp").item(0).getTextContent()+"</td>");
					//Step Title
					reportPrintWriter.println("<td>"+eElement.getElementsByTagName("stepName").item(0).getTextContent()+"</td>");
					//Step Description
					if(eElement.getElementsByTagName("stepStackTrace").getLength()>0){
						reportPrintWriter.println("<td align=center><a href=\"javascript:;\" onclick=\"toggleObjectVisibility(document.getElementById('stackTrace"+i+"'))\">Show/Hide Stack Trace</a>"
								+ "<p id=\"stackTrace"+i+"\" style=\"display: none\">"+eElement.getElementsByTagName("stepDesc").item(0).getTextContent().replace("/n ", "<br>")+"</p>");
					}else{
						reportPrintWriter.println("<td align=center>"+eElement.getElementsByTagName("stepDesc").item(0).getTextContent().replace("/n ", "<br>"));
					}
					//Screenshot
					if(eElement.getElementsByTagName("stepScreenshot").getLength()>0){
					    reportPrintWriter.println("<br><a href='file:///"+eElement.getElementsByTagName("stepScreenshot").item(0).getTextContent()+"'><img src='file:///"+eElement.getElementsByTagName("stepScreenshot").item(0).getTextContent()+"' height='50%' width='50%'></a>");
					}
					reportPrintWriter.println("</td>");
					reportPrintWriter.println("</tr>");
				}
				reportPrintWriter.println("</table>");
				reportPrintWriter.println("<br>");
			 
			}catch (Exception e) {
				e.printStackTrace();
		    }
			//Populate summary Table
			reportPrintWriter.println("<script type =text/javascript>");
			reportPrintWriter.println("var startTime = new Date(new Date().toDateString() + ' ' + document.getElementById(\"resultsTable"+i+"\").rows[1].cells[2].innerHTML);");
			reportPrintWriter.println("var rowCount = document.getElementById(\"resultsTable"+i+"\").rows.length - 1;");
			reportPrintWriter.println("var endTime = new Date(new Date().toDateString() + ' ' + document.getElementById(\"resultsTable"+i+"\").rows[rowCount].cells[2].innerHTML);");
			reportPrintWriter.println("document.getElementById(\"summaryTable"+i+"\").rows[1].cells[0].innerHTML = getStatus()");
			reportPrintWriter.println("var runTime = endTime - startTime;");
			reportPrintWriter.println("var seconds = parseInt((runTime / 1000) % 60);");
			reportPrintWriter.println("var minutes = parseInt(runTime / (1000*60) % 60);");
			reportPrintWriter.println("var hours = parseInt(runTime / (1000*60*60) % 24);");
			reportPrintWriter.println("document.getElementById(\"summaryTable"+i+"\").rows[1].cells[1].innerHTML = hours + \" hours:\" + minutes + \" minutes:\" + seconds +\" seconds\";");
			//GetStatus Function
			reportPrintWriter.println("function getStatus(){");
			reportPrintWriter.println("var warning = false;");
			reportPrintWriter.println("for(i=0;i<document.getElementById(\"resultsTable"+i+"\").rows.length-1;i++){");
			reportPrintWriter.println("if(document.getElementById(\"resultsTable"+i+"\").rows[i].cells[1].innerHTML==String.fromCharCode(10007)){");
			reportPrintWriter.println("document.getElementById(\"totalFailed\").innerHTML = parseInt(document.getElementById(\"totalFailed\").innerHTML) + 1;");
			reportPrintWriter.println("return \"Failed\"}");
			reportPrintWriter.println("else if(document.getElementById(\"resultsTable"+i+"\").rows[i].cells[1].innerHTML==\"!\"){warning = true}");
			reportPrintWriter.println("}");
			reportPrintWriter.println("if(warning){document.getElementById(\"totalWarnings\").innerHTML = parseInt(document.getElementById(\"totalWarnings\").innerHTML) + 1;");
			reportPrintWriter.println("return \"Warning\"");
			reportPrintWriter.println("}else{document.getElementById(\"totalPassed\").innerHTML = parseInt(document.getElementById(\"totalPassed\").innerHTML) + 1;");
			reportPrintWriter.println("return \"Passed\"}");
			reportPrintWriter.println("}");
			reportPrintWriter.println("</script>");
		}
		reportPrintWriter.println("</body>");
		//Toggle Visibility
		reportPrintWriter.println("<script>");
		reportPrintWriter.println("function toggleObjectVisibility(oElement){");
		reportPrintWriter.println("if(oElement.style.display == \"\"){");
		reportPrintWriter.println("oElement.style.display = \"none\";");
		reportPrintWriter.println("}else{");
		reportPrintWriter.println("oElement.style.display = \"\";");
		reportPrintWriter.println("}");
		reportPrintWriter.println("}");
		reportPrintWriter.println("</script>");
		//Generate Pie Chart
		//See: https://developers.google.com/chart/interactive/docs/gallery/piechart
		reportPrintWriter.println("<script type=\"text/javascript\" src=\"https://www.google.com/jsapi\"></script>");
		reportPrintWriter.println("<script type=\"text/javascript\">");
		reportPrintWriter.println("var nPassed = parseInt(document.getElementById('totalPassed').innerHTML);");
		reportPrintWriter.println("var nFailed = parseInt(document.getElementById('totalFailed').innerHTML);");
		reportPrintWriter.println("var nWarning = parseInt(document.getElementById('totalWarnings').innerHTML);");
		reportPrintWriter.println("google.load(\"visualization\", \"1\", {packages:[\"corechart\"]});");
		reportPrintWriter.println("google.setOnLoadCallback(drawChart);");
		reportPrintWriter.println("function drawChart() {");
		reportPrintWriter.println("var data = google.visualization.arrayToDataTable([");
		reportPrintWriter.println("['Results', 'Count'],");
		reportPrintWriter.println("['Passed', nPassed],");
		reportPrintWriter.println("['Failed', nFailed],");
		reportPrintWriter.println("['Warning', nWarning],");
		reportPrintWriter.println("]);");
		reportPrintWriter.println("var options = {");
		reportPrintWriter.println("chartArea:{left:'auto',top:10,width:'50%',height:'75%'},");
		reportPrintWriter.println("legend: 'bottom',");
		reportPrintWriter.println("slices:{");
		reportPrintWriter.println("0: {color:'green'},");
		reportPrintWriter.println("1: {color:'red'},");
		reportPrintWriter.println("2: {color:'orange'}");
		reportPrintWriter.println("}");
		reportPrintWriter.println("};");
		reportPrintWriter.println("var chart = new google.visualization.PieChart(document.getElementById('piechart'));");
		reportPrintWriter.println("chart.draw(data, options);");
		reportPrintWriter.println("}");
		reportPrintWriter.println("</script>");
		reportPrintWriter.println("</html>");
		reportPrintWriter.close();
		
		//rename file
		String outputFileName = outputName;
		File file2 = new File("C:\\Temp\\"+ outputFileName+".html");
		if(file2.exists()){
			file2.delete();
		}
		htmlFile.renameTo(file2);
		Reporter.setEscapeHtml(false);
		Reporter.log("<a href="+file2.getAbsolutePath()+">"+file2.getAbsolutePath()+"</a>");
	}
}
