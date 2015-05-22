package com.orasi.apps.bluesource;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import ru.yandex.qatools.allure.annotations.Parameter;
import ru.yandex.qatools.allure.annotations.Step;

import com.orasi.core.interfaces.Button;
import com.orasi.core.interfaces.Listbox;
import com.orasi.core.interfaces.Textbox;
import com.orasi.core.interfaces.impl.internal.ElementFactory;
import com.orasi.utils.PageLoaded;
import com.orasi.utils.TestEnvironment;

public class NewTitlePage {
    	private TestEnvironment te = null;

	//All the page elements
	@FindBy(id = "title_name")
	private Textbox txtTitle;
	
	@FindBy(name = "commit")
	private Button btnCreateTitle;
	
	// *********************
	// ** Build page area **
	// *********************
	public NewTitlePage(TestEnvironment te){
		this.te = te;
		ElementFactory.initElements(te.getDriver(), this);
	}

	// *****************************************
	// ***Page Interactions ***
	// *****************************************

	
	//method to create a new title
	@Step("When I create the new title \"{0}\"")
	public void createNewTitle(@Parameter String newTitle){
		txtTitle.safeSet(newTitle);
		btnCreateTitle.click();
	}

}
