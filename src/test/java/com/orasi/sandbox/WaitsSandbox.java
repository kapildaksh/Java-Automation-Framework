package com.orasi.sandbox;

import javax.xml.xpath.XPathExpressionException;

import org.json.JSONException;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.orasi.api.restServices.core.RestService;

public class WaitsSandbox {
	RestService rest = null;	
	
	@Test()
	public void main() throws XPathExpressionException, JSONException{
		rest = new RestService();
		rest.setDefaultResponseFormat("xml");
		rest.sendGetRequest("https://maps.googleapis.com/maps/api/geocode/xml?sensor=false&address=7025 Albert Pick Rd Greensboro NC, 27409","xml");
		
		Assert.assertEquals(rest.getXmlResponseByXpath("/GeocodeResponse/result/address_component[1]/long_name"), "7025");
		Assert.assertEquals(rest.getXmlResponseByXpath("/GeocodeResponse/result/address_component[2]/short_name"), "Albert Pick Rd");
		Assert.assertEquals(rest.getXmlResponseByXpath("/GeocodeResponse/result/address_component[8]/long_name"), "27409");
		
		int numberOfNodes = rest.getNumberOfNodesByXpath("/GeocodeResponse/result/address_component");
		validateAddressValue("street_number", "7025", numberOfNodes);
		validateAddressValue("route", "Albert Pick Road", numberOfNodes);
		validateAddressValue("postal_code", "27409", numberOfNodes);
		validateAddressValue("political", "Greensboro", numberOfNodes);
	}
	
	private void validateAddressValue(String tagetValue, String value, int maxIndex) throws XPathExpressionException{
		String path = "";
		int numberOfNodes = 0;
		boolean nodeFound = false;
		
		for(int loopCounter = 1; loopCounter <= maxIndex; loopCounter++){
			path = "/GeocodeResponse/result/address_component["+String.valueOf(loopCounter)+"]/type";
			numberOfNodes = rest.getNumberOfNodesByXpath(path);
			if(numberOfNodes > 1){
				for(int nodeCounter = 1; nodeCounter <= numberOfNodes; nodeCounter++){
					path = "/GeocodeResponse/result/address_component["+String.valueOf(loopCounter)+"]/type["+String.valueOf(nodeCounter)+"]";
					if(rest.getXmlResponseByXpath(path).equalsIgnoreCase(tagetValue)){
						path = path.replace("/type["+String.valueOf(nodeCounter)+"]", "/long_name");
						Assert.assertEquals(rest.getXmlResponseByXpath(path), value, "For the target value of ["+tagetValue+"] the value of ["+value+"] was expected, but ["+rest.getXmlResponseByXpath(path)+"] was found");
						nodeFound = true;
						break;
					}
				}
				if(nodeFound){
					break;
				}
			}else{
				if(rest.getXmlResponseByXpath(path).equalsIgnoreCase(tagetValue)){
					path = path.replace("/type", "/long_name");
					Assert.assertEquals(rest.getXmlResponseByXpath(path), value);
					break;
				} 
			}
		}
	}
}
