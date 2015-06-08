package com.orasi.utils;
import org.openqa.selenium.WebDriver;

import com.orasi.apps.bluesource.DepartmentsPage;
import com.orasi.apps.bluesource.LoginPage;
import com.orasi.apps.bluesource.NewDeptPage;
import com.orasi.apps.bluesource.TopNavigationBar;
import com.orasi.apps.bluesource.employeesPage.EmployeesPage;
import com.orasi.apps.bluesource.employeesPage.ManageEmployeeModal;
import com.orasi.apps.bluesource.titlesPage.ListingTitlesPage;
import com.orasi.apps.bluesource.titlesPage.NewTitlePage;
import com.orasi.core.interfaces.Element;
import com.orasi.core.interfaces.impl.internal.ElementFactory;

public class Page extends ElementFactory{
    protected TestEnvironment te = null;
    
    public Page(){};
    
    public Page(TestEnvironment te){
	this.te =te;
    }
    
    public WebDriver getDriver(){
	return te.getDriver();
    }

    
    // ************************************
    // ************************************
    // ************************************
    // PAGE Instantiations
    // ************************************
    // ************************************
    // ************************************    
    public LoginPage loginPage(){
	return new LoginPage(te);
    }
    
    public TopNavigationBar topNavigationBar(){
  	return new TopNavigationBar(te);
      }
    
    public EmployeesPage employeesPage(){
  	return new EmployeesPage(te);
      }
    
    public ManageEmployeeModal manageEmployee(){
  	return new ManageEmployeeModal(te);
      }
    
    public DepartmentsPage departmentsPage(){
  	return new DepartmentsPage(te);
      } 
    
    public NewDeptPage newDeptPage(){
  	return new NewDeptPage(te);
      }
    
    public NewTitlePage newTitlePage(){
  	return new NewTitlePage(te);
      } 
    
    public ListingTitlesPage listingTitlesPage(){
  	return new ListingTitlesPage(te);
      }
    
    // ************************************
    // ************************************
    // ************************************
    // PAGE OBJECT METHODS
    // ************************************
    // ************************************
    // ************************************

    /**
     * @summary loops for a predetermined amount of time (defined by
     *          WebDriverSetup.getDefaultTestTimeout()) to determine if the DOM
     *          is in a ready-state
     * @return boolean: true is the DOM is completely loaded, false otherwise
     * @param N
     *            /A
     */
    protected boolean pageLoaded() {
	return new PageLoaded().isDomComplete(getDriver());
    }

    /**
     * @summary loops for a predetermined amount of time (defined by
     *          WebDriverSetup.getDefaultTestTimeout()) to determine if the
     *          Element is not null
     * @return boolean: true is the DOM is completely loaded, false otherwise
     * @param clazz
     *            - page class that is calling this method
     * @param element
     *            - element with which to determine if a page is loaded
     */
    protected boolean pageLoaded(Class<?> clazz, Element element) {

	return new PageLoaded().isElementLoaded(clazz, getDriver(), element);
    }

    /**
     * @summary Used to create all page objects WebElements as proxies (not
     *          actual objects, but rather placeholders) or to reinitialize all
     *          page object WebElements to allow for the state of a page to
     *          change and not fail a test
     * @return N/A
     * @param clazz
     *            - page class that is calling this method for which to
     *            initialize elements
     */
    protected void initializePage(Class<?> clazz) {
	try {
	    initElements(getDriver(), clazz.getConstructor(TestEnvironment.class));
	} catch (NoSuchMethodException | SecurityException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
}
