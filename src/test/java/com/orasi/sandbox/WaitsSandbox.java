package com.orasi.sandbox;

import javax.xml.xpath.XPathExpressionException;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.orasi.api.restServices.core.RestService;

public class WaitsSandbox {
	RestService rest = null;	
	
	@Test()
	public void main() throws XPathExpressionException{
		rest = new RestService();
		rest.sendGetRequest("https://maps.googleapis.com/maps/api/geocode/xml?sensor=false&address=7025 Albert Pick Rd Greensboro NC, 27409","xml");
		
		Assert.assertEquals(rest.getXmlResponseByXpath("/GeocodeResponse/result/address_component[1]/long_name"), "7025");
		Assert.assertEquals(rest.getXmlResponseByXpath("/GeocodeResponse/result/address_component[2]/short_name"), "Albert Pick Rd");
		Assert.assertEquals(rest.getXmlResponseByXpath("/GeocodeResponse/result/address_component[8]/long_name"), "27409");
		
		validateStreetNumber("7025", rest.getNumberOfNodesByXpath("/GeocodeResponse/result/address_component"));
	}
	
	private void validateStreetNumber(String streetNumber, int maxIndex) throws XPathExpressionException{
		String path = "";
		int numberOfNodes = 0;
		
		for(int loopCounter = 1; loopCounter <= maxIndex; loopCounter++){
			path = "/GeocodeResponse/result/address_component["+String.valueOf(loopCounter)+"]/type";
			numberOfNodes = rest.getNumberOfNodesByXpath(path);
			if(numberOfNodes > 1){
			}else{
				if(rest.getXmlResponseByXpath(path).equalsIgnoreCase("street_number")){
					path = path.replace("/type", "/long_name");
					Assert.assertEquals(rest.getXmlResponseByXpath(path), streetNumber);
					break;
				} 
			}
		}
	}
}
