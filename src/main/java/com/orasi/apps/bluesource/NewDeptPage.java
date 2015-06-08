package com.orasi.apps.bluesource;

import org.openqa.selenium.support.FindBy;

import ru.yandex.qatools.allure.annotations.Step;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.Page;
import com.orasi.utils.TestEnvironment;
public class NewDeptPage extends Page{

	//All the page elements
	@FindBy(id = "department_name")
	private Textbox txtDept;
	
	@FindBy(id = "department_department_id")
	private Textbox txtParentDept;
	
	@FindBy(name = "commit")
	private Button btnCreateDept;
	
	// *********************
	// ** Build page area **
	// *********************
	public NewDeptPage(TestEnvironment te){
		super(te);
		initElements(te.getDriver(), this);
	}
	
	public boolean pageLoaded(){
	    return te.pageLoaded().isElementLoaded(this.getClass(), btnCreateDept);
	}
	
	// *****************************************
	// ***Page Interactions ***
	// *****************************************
	
	
	//method to create a new title
	@Step("When I create the new department \"{0}\"")
	public void createNewDept(String dept){
		txtDept.safeSet(dept);
		btnCreateDept.click();
	}
}
