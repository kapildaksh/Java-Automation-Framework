package com.orasi.apps.bluesource;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.testng.Reporter;

import com.orasi.utils.PageLoaded;
import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Listbox;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.impl.internal.ElementFactory;




public class AddNewEmployeePage {

	private WebDriver driver;
	private StringBuffer verificationErrors = new StringBuffer();

	//All the page elements:
	@FindBy(id = "employee_username")	
	private Textbox txtUsername;
	
	@FindBy(id = "employee_first_name")
	private Textbox txtFirstName;
	
	@FindBy(id = "employee_last_name")
	private Textbox txtLastName;
	
	@FindBy(id = "employee_title_id")
	private Listbox lstTitle;
	
	@FindBy(id = "employee_role")
	private Listbox lstRole;
	
	@FindBy(id = "employee_manager_id")
	private Listbox lstManager;
	
	@FindBy(id = "employee_status")
	private Listbox lstStatus;
	
	@FindBy(id = "employee_location")
	private Listbox lstLocation;
	
	@FindBy(id = "employee_start_date")
	private Textbox txtStartDate;
	
	@FindBy(id = "employee_cell_phone")
	private Textbox txtCellPhone;
	
	@FindBy(id = "employee_office_phone")
	private Textbox txtOfficePhone;
	
	@FindBy(id = "employee_email")
	private Textbox txtEmail;
	
	@FindBy(id = "employee_department_id")
	private Listbox lstDept;
	
	@FindBy(name = "commit")
	private Button btnCreateEmp;

	// *********************
	// ** Build page area **
	// *********************
	public AddNewEmployeePage(WebDriver driver){
		this.driver = driver;
		ElementFactory.initElements(driver, this);
	}
	
	public boolean pageLoaded(){
		return new PageLoaded().isElementLoaded(this.getClass(), driver, txtUsername); 
		  
	}
	
	public AddNewEmployeePage initialize() {
		return ElementFactory.initElements(driver,
				this.getClass());       
	 }

	// *****************************************
	// ***Page Interactions ***
	// *****************************************
	
	//adds a new employee on the new employee page
	public void addEmployee(String username, String firstName, String lastName, String title, String role, String manager,
							String status, String location, String startDate, String cellPhone, String officePhone, 
							String email, String dept) throws Exception {
		  
		  //Fill in the details
		  try {
			  txtUsername.safeSet(username);
			  txtUsername.safeSet(username);
			  txtFirstName.safeSet(firstName);
			  txtLastName.safeSet(lastName);
			  lstTitle.select(title);
			  lstRole.select(role);
			  lstManager.select(manager);
			  lstStatus.select(status);
			  lstLocation.select(location);
			  txtStartDate.safeSet(startDate);
			  txtCellPhone.safeSet(cellPhone);
			  txtOfficePhone.safeSet(officePhone);
			  txtEmail.safeSet(email);
			  lstDept.select(dept);
		  
			  //submit
			  btnCreateEmp.click();
			  
		  }catch (Exception e){
			  verificationErrors.append(e.toString());
			  Reporter.log("Element not found on the add employee frame: " + e);
			  throw(e);
		  }
		  
	  }
}
