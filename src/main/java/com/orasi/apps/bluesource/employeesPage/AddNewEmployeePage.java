package com.orasi.apps.bluesource.employeesPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;

import ru.yandex.qatools.allure.annotations.Step;

import com.orasi.utils.PageLoaded;
import com.orasi.utils.TestEnvironment;
import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Label;
import com.orasi.core.interfaces.Listbox;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.impl.internal.ElementFactory;

public class AddNewEmployeePage {

	private TestEnvironment te;
	private StringBuffer verificationErrors = new StringBuffer();

	//All the page elements:
	
	@FindBy(id = "new_employee")
	private Label lblAddEmployeePopup;
	
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
	
	@FindBy(id = "employee_im_name")
	private Textbox txtImName;
	
	@FindBy(id = "employee_im_client")
	private Listbox lstImClient;
	
	@FindBy(id = "employee_department_id")
	private Listbox lstDept;
	
	@FindBy(name = "commit")
	private Button btnCreateEmp;

	// *********************
	// ** Build page area **
	// *********************
	public AddNewEmployeePage(TestEnvironment te){
		this.te = te;
		ElementFactory.initElements(te.getDriver(), this);
	}
	
	public boolean pageLoaded(){
		return te.pageLoaded().isElementLoaded(this.getClass(), txtUsername); 		  
	}
	
	// *****************************************
	// ***Page Interactions ***
	// *****************************************
	
	//adds a new employee on the new employee page
	@Step("And I add a new Employee")
	public void addEmployee(String username, String firstName, String lastName, String title, String role, String manager,
							String status, String location, String startDate, String cellPhone, String officePhone, 
							String email, String imName, String imClient, String dept)  {
	    lblAddEmployeePopup.syncEnabled(te.getDriver());
		  //Fill in the details
		  try {
			  txtUsername.set(username);
			  txtFirstName.set(firstName);
			  txtLastName.set(lastName);
			  lstTitle.select(title);
			  lstRole.select(role);
			  lstManager.select(manager);
			  lstStatus.select(status);
			  lstLocation.select(location);
			  txtStartDate.safeSet(startDate);
			  txtCellPhone.set(cellPhone);
			  txtOfficePhone.set(officePhone);
			  txtEmail.set(email);
			  txtImName.set(imName);
			  lstImClient.select(imClient);
			  lstDept.select(dept);
		  
			  //submit
			  btnCreateEmp.click();
			  
		  }catch (Exception e){
			  verificationErrors.append(e.toString());
			  Reporter.log("Element not found on the add employee frame: " + e);
			  throw new RuntimeException(e);
		  }
		  
	  }
	
	public void addEmployee(Employee employee){
	    addEmployee(employee.getUsername(), employee.getFirstName(), employee.getLastName(),employee.getTitle(), employee.getRole(), employee.getManager(),
		    employee.getStatus(),employee.getLocation(),employee.getStartDate(),employee.getCellPhone(),employee.getOfficePhone(),employee.getEmail(),
		    employee.getImName(),employee.getImClient(),employee.getDepartment());   		   
	}
}
