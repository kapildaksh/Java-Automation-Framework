package com.orasi.apps.bluesource.employeesPage;

import org.openqa.selenium.support.FindBy;

import ru.yandex.qatools.allure.annotations.Step;

import com.orasi.core.by.angular.FindByNG;
import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Link;
import com.orasi.core.interfaces.Webtable;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.TestEnvironment;
import com.orasi.utils.TestReporter;
import com.orasi.utils.date.DateTimeConversion;

public class EmployeeSummaryPage {
	private TestEnvironment te;
	
	//All the page elements
	
	@FindBy( xpath= "//div[contains(@class, 'panel-heading') and contains(@data-target,'#panel_body_1')]")
	private Label lblGeneralInfo;
	
	@FindBy(id = "panel_body_1")
	private Label lblGeneralInfoBody;
	
	@FindBy(xpath = "//div[@id='panel_body_1']/div/table")
	private Webtable tabGeneralInfoTable;
	
	@FindByNG(ngButtonText = "Manage")
	private Button btnManageGeneralInfo;
	
	@FindBy( xpath= "//div[contains(@class, 'panel-heading') and contains(@data-target,'#panel_body_2')]")
	private Label lblProjectInfo;
	
	@FindBy(id = "panel_body_2")
	private Label lblProjectInfoBody;
	
	@FindBy(partialLinkText = "projects" )
	private Button btnManageProjectInfo;
	
	@FindBy( xpath= "//div[contains(@class, 'panel-heading') and contains(@data-target,'#panel_body_3')]")
	private Label lblTimeOffInfo;
	
	@FindBy(id = "panel_body_3")
	private Label lblTimeOffInfoBody;
	
	@FindBy(xpath = "//*[@id='accordion']/div/div[6]/div[1]/a[2]")
	private Link lnkManageTimeOff;
	
	@FindBy(linkText = "View")
	private Link lnkViewTimeOff;
	
	
	/*
	 * General info rows
	 */
	private final int USERNAME = 1;
	private final int ROLE = 2;
	private final int TITLE = 3;
	private final int MANAGER = 4;
	private final int STATUS = 5;
	private final int LOCATION = 6;
	private final int START_DATE = 7;
	private final int TIME_WITH_ORASI = 8;
	private final int CELLPHONE = 9;
	private final int OFFICEPHONE = 10;
	private final int EMAIL = 11;
	private final int IM_USER = 12;
	private final int IM_CLIENT = 13;
	private final int DEPARTMENT = 14;
	
	// *********************
	// ** Build page area **
	// *********************
	public EmployeeSummaryPage(TestEnvironment te){
		this.te = te;
		ElementFactory.initElements(te.getDriver(), this);
	}
		
	public boolean pageLoaded(){
		return te.pageLoaded().isElementLoaded(this.getClass(), lnkViewTimeOff); 	    
	}
	// *****************************************
	// ***Page Interactions ***
	// *****************************************

	public void ClickManageTimeOff(){
		lnkManageTimeOff.click();
	}
	
	public void ViewManageTimeOff(){
		lnkViewTimeOff.click();
	}
	
	public void viewGeneralInfo(){
		if(lblGeneralInfoBody.getAttribute("class").equals("panel-collapse collapse")) lblGeneralInfo.click();		
	}
	
	public void viewProjectsInfo(){
		if(lblProjectInfoBody.getAttribute("class").equals("panel-collapse collapse")) lblProjectInfo.click();
	}
	
	public void viewTimeOffInfo(){
		if(lblTimeOffInfoBody.getAttribute("class").equals("panel-collapse collapse")) lblTimeOffInfo.click();
	}
	
	@Step("Then the Employees General Information is correct")
	public boolean validateGeneralInfo(Employee employee){
		viewGeneralInfo();
		
		String convertedStartDate = DateTimeConversion.convert(employee.getStartDate(), "yyyy-MM-dd", "MMMM dd, yyyy");

		if (!employee.getUsername().equalsIgnoreCase(tabGeneralInfoTable.getCellData(te, USERNAME, 2))) {TestReporter.logFailure("User name did not match"); return false;}
		if (!employee.getRole().equalsIgnoreCase(tabGeneralInfoTable.getCellData(te,ROLE, 2))) {TestReporter.logFailure("Role did not match"); return false;}
		if (!employee.getTitle().equalsIgnoreCase(tabGeneralInfoTable.getCellData(te, TITLE, 2))) {TestReporter.logFailure("Title did not match"); return false;}
		if (!employee.getManager().equalsIgnoreCase(tabGeneralInfoTable.getCellData(te, MANAGER, 2))) {TestReporter.logFailure("Manager did not match"); return false;}
		if (!employee.getStatus().equalsIgnoreCase(tabGeneralInfoTable.getCellData(te, STATUS, 2))) {TestReporter.logFailure("Status did not match"); return false;}
		if (!employee.getLocation().equalsIgnoreCase(tabGeneralInfoTable.getCellData(te, LOCATION, 2))) {TestReporter.logFailure("Location did not match"); return false;}
		if (!convertedStartDate.equals(tabGeneralInfoTable.getCellData(te, START_DATE, 2))) {TestReporter.logFailure("Start Date did not match"); return false;}
		//if (!employee.getStartDate().equalsIgnoreCase(tabGeneralInfoTable.getCellData(te, TIME_WITH_ORASI, 2))) {TestReporter.logFailure("Time With Orasi did not match"); return false;}
		if (!employee.getCellPhone().equalsIgnoreCase(tabGeneralInfoTable.getCellData(te, CELLPHONE, 2))) {TestReporter.logFailure("Cell Phone did not match"); return false;}
		if (!employee.getOfficePhone().equalsIgnoreCase(tabGeneralInfoTable.getCellData(te, OFFICEPHONE, 2))) {TestReporter.logFailure("Office Phone did not match"); return false;}
		if (!employee.getEmail().equalsIgnoreCase(tabGeneralInfoTable.getCellData(te, EMAIL, 2))) {TestReporter.logFailure("Email did not match"); return false;}
		if (!employee.getImName().equalsIgnoreCase(tabGeneralInfoTable.getCellData(te, IM_USER, 2))) {TestReporter.logFailure("IM Username did not match"); return false;}
		if (!employee.getImClient().equalsIgnoreCase(tabGeneralInfoTable.getCellData(te, IM_CLIENT, 2))) {TestReporter.logFailure("IM Client did not match"); return false;}
		if (!employee.getDepartment().equalsIgnoreCase(tabGeneralInfoTable.getCellData(te, DEPARTMENT, 2))) {TestReporter.logFailure("Department did not match"); return false;}
				
		return true;
	}
	
	@Step("And I click Manage General Info")
	public void clickManageGeneralInfo(){
	    btnManageGeneralInfo.click();
	}
}
