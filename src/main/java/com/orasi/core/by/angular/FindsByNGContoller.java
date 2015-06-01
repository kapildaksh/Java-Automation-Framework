package com.orasi.core.by.angular;
import java.util.List;

import org.openqa.selenium.WebElement;

	public interface FindsByNGContoller {
	  WebElement findElementByNGContoller(String using);
	  List<WebElement> findElementsByNGContoller(String using);
	}

