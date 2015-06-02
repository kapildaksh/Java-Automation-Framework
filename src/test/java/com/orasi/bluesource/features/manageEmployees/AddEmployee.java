package com.orasi.bluesource.features.manageEmployees;

import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Parameter;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.annotations.Title;
import ru.yandex.qatools.allure.model.SeverityLevel;

import com.orasi.apps.bluesource.LoginPage;
import com.orasi.apps.bluesource.TopNavigationBar;
import com.orasi.apps.bluesource.employeesPage.Employee;
import com.orasi.apps.bluesource.employeesPage.EmployeeSummaryPage;
import com.orasi.apps.bluesource.employeesPage.EmployeesPage;
import com.orasi.apps.bluesource.employeesPage.ManageEmployeeModal;
import com.orasi.utils.Constants;
import com.orasi.utils.Randomness;
import com.orasi.utils.TestEnvironment;
import com.orasi.utils.TestReporter;
import com.orasi.utils.dataProviders.ExcelDataProvider;

public class AddEmployee  extends TestEnvironment {

    private String application = "Bluesource";
    private Employee employee = new Employee();
    
    @DataProvider(name = "dataScenario")
    public Object[][] scenarios() {
	return new ExcelDataProvider(Constants.BLUESOURCE_DATAPROVIDER_PATH
		+ "ManageEmployees.xlsx", "AddEmployee").getTestData();
    }

    @BeforeTest(groups = { "regression", "manageEmployees", "employeeCRUD" })
    @Parameters({ "runLocation", "browserUnderTest", "browserVersion",
	    "operatingSystem", "environment" })
    public void setup(@Optional String runLocation, String browserUnderTest,
	    String browserVersion, String operatingSystem, String environment) {
	setApplicationUnderTest(application);
	setBrowserUnderTest(browserUnderTest);
	setBrowserVersion(browserVersion);
	setOperatingSystem(operatingSystem);
	setRunLocation(runLocation);
	setTestEnvironment(environment);
    }

    @AfterMethod(groups = { "regression", "manageEmployees", "employeeCRUD" })
    public void closeSession(ITestResult result) {
	if(!result.isSuccess() || result.getMethod().getMethodName().equals("testDeactivateEmployee"))
	endTest(testName);
    }

    /**
     * @Summary: Creates an Employee
     * @Precondition:NA
     * @Author: Jessica Marshall
     * @Version: 10/6/2014
     * @Return: N/A
     */
    @Features("ManageEmployees")
    @Stories("I can create a new Employee")
    @Severity(SeverityLevel.BLOCKER)
    @Title("AddEmployee")
    @Test(dataProvider = "dataScenario", groups = { "regression", "manageEmployees", "employeeCRUD" })
    public void testAddEmployee(@Parameter String testScenario, @Parameter String role) {
	
	setTestName(new Object() {}.getClass().getEnclosingMethod().getName());

	testStart(testName);
	
	// Login
	LoginPage loginPage = new LoginPage(this);
	TestReporter.assertTrue(loginPage.pageLoaded(),"Verify login page is displayed");
	loginPage.login(role);

	// Verify user is logged in
	TopNavigationBar topNavigationBar = new TopNavigationBar(this);
	TestReporter.assertTrue(topNavigationBar.isLoggedIn(), "Validate the user logged in successfully");

	//Navigate to Employees Page
	topNavigationBar.clickEmployeesLink();
	EmployeesPage employeesPage = new EmployeesPage(this);
	TestReporter.assertTrue(employeesPage.pageLoaded(),"Verify Employees page is displayed");

	// Open New Employee Modal 
	employeesPage.clickAddEmployeeButton();
	ManageEmployeeModal newEmployee = new ManageEmployeeModal(this);	
	TestReporter.assertTrue(newEmployee.pageLoaded(),"Verify New Employee Popup Modal is displayed");
	
	//Enter Employee Info
	newEmployee.addEmployee(employee);
	TestReporter.assertTrue(employeesPage.isSuccessMsgDisplayed(), "Verify Success message appears after creating new employee");
	
	//Validate new Employee created and visible
	employeesPage.enterSearchText(employee.getLastName());
	TestReporter.assertTrue(employeesPage.validateLastnameFoundInTable(employee.getLastName()), "Verify Employee " + employee.getLastName() + " appeared in the Employee Table");
    }    
    
    
    @Features("ManageEmployees")
    @Stories("I can see an Employee's General Info after creating Employee")
    @Severity(SeverityLevel.NORMAL)
    @Title("ModifyEmployeeInfo")
    @Test(groups = { "regression", "manageEmployees", "employeeCRUD" },
    	  dependsOnMethods = {"testAddEmployee"})
    public void testViewEmployeeGeneralInfo() {
	EmployeesPage employeesPage = new EmployeesPage(this);
	employeesPage.selectEmployeeName(employee.getLastName());
	
	EmployeeSummaryPage summary = new EmployeeSummaryPage(this);
	TestReporter.assertTrue(summary.pageLoaded(),"Verify Employees Summary page is displayed");
	TestReporter.assertTrue(summary.validateGeneralInfo(employee), "Verify Employee's General Information is correct");
	
    }
    
    
    @Features("ManageEmployees")
    @Stories("I can Modify an Employee's General Info and view changes")
    @Severity(SeverityLevel.MINOR)
    @Title("ModifyEmployeeInfo")
    @Test(groups = { "regression", "manageEmployees", "employeeCRUD" },
    	  dependsOnMethods = {"testViewEmployeeGeneralInfo"})
    public void testModifyEmployeeGeneralInfo() {
	EmployeeSummaryPage summary = new EmployeeSummaryPage(this);
	summary.clickManageGeneralInfo();
	
	ManageEmployeeModal modifyEmployee = new ManageEmployeeModal(this);
	TestReporter.assertTrue(modifyEmployee.pageLoaded(),"Verify Manage Employee Popup Modal is displayed");
	
	employee.setLastName(Randomness.randomAlphaNumeric(8));
	employee.setTitle("Software Consultant");
	modifyEmployee.modifyEmployee(employee);
	TestReporter.assertTrue(summary.validateGeneralInfo(employee), "Verify Employee's General Information is correct");
    }
    
    
    @Features("ManageEmployees")
    @Stories("I can mark an Employee as Inactive")
    @Severity(SeverityLevel.MINOR)
    @Title("MarkEmployeeInactive")
    @Test(groups = { "regression", "manageEmployees", "employeeCRUD" },
    	  dependsOnMethods = {"testModifyEmployeeGeneralInfo"})
    public void testDeactivateEmployee() {
	EmployeeSummaryPage summary = new EmployeeSummaryPage(this);
	summary.clickManageGeneralInfo();
	
	ManageEmployeeModal modifyEmployee = new ManageEmployeeModal(this);
	TestReporter.assertTrue(modifyEmployee.pageLoaded(),"Verify Manage Employee Popup Modal is displayed");
	
	employee.setStatus("Inactive");
	modifyEmployee.modifyEmployee(employee);
	TestReporter.assertTrue(summary.validateGeneralInfo(employee), "Verify Employee's General Information is correct");
	
	TopNavigationBar topNavigationBar = new TopNavigationBar(this);
	topNavigationBar.clickEmployeesLink();
	
	EmployeesPage employeesPage = new EmployeesPage(this);
	TestReporter.assertTrue(employeesPage.pageLoaded(),"Verify Employees page is displayed");
	employeesPage.enterSearchText(employee.getLastName());
	TestReporter.assertTrue(employeesPage.validateNoRowsFound(),"Verify Employees Table does not have Employee as active");
	topNavigationBar.clickLogout();
    }
}
