package com.orasi.api.SoapExamples.predict8.CRMServicePTBinding.operations;

import com.orasi.api.SoapExamples.predict8.CRMServicePTBinding.CRMServicePTBinding;
import com.orasi.utils.XMLTools;
import com.orasi.utils.Constants;

public class Create extends CRMServicePTBinding{
	public Create(String environment, String scenario) {
		super(environment);
		
		//Generate a request from a project xml file
		setRequestDocument(XMLTools.loadXML(buildRequestFromWSDL("create")));
		System.out.println(getRequest());
				
		setRequestNodeValueByXPath(getTestScenario(Constants.PREDIC8API_DATAPROVIDER_PATH + 
				"crmServicePTBinding.xls", scenario));
		removeComments() ;
		removeWhiteSpace();
	}
	
	/*
	 * Customer Personal Data
	 */
	public String getPersonId(){
		return getRequestNodeValueByXPath("/Envelope/Body/create/customer/person/id");
	}
	
	public String getPersonFirstName(){
		return getRequestNodeValueByXPath("/Envelope/Body/create/customer/person/firstName");
	}
	
	public String getPersonLastName(){
		return getRequestNodeValueByXPath("/Envelope/Body/create/customer/person/lastName");
	}
	
	public String getPersonAddressStreet(){
		return getRequestNodeValueByXPath("/Envelope/Body/create/customer/person/address/street");
	}
	
	public String getPersonAddressCity(){
		return getRequestNodeValueByXPath("/Envelope/Body/create/customer/person/address/city");
	}
	
	public String getPersonAddressZipCode(){
		return getRequestNodeValueByXPath("/Envelope/Body/create/customer/person/address/zipCode");
	}
	
	public String getPersonAddressCountry(){
		return getRequestNodeValueByXPath("/Envelope/Body/create/customer/person/address/country");
	}
	
	public String getPersonAge(){
		return getRequestNodeValueByXPath("/Envelope/Body/create/customer/person/age");
	}
	
	/*
	 * Customer Address Data
	 */
	public String getAddressStreet(){
		return getRequestNodeValueByXPath("/Envelope/Body/create/customer/address/street");
	}
	
	public String getAddressCity(){
		return getRequestNodeValueByXPath("/Envelope/Body/create/customer/address/city");
	}
	
	public String getAddressZipCode(){
		return getRequestNodeValueByXPath("/Envelope/Body/create/customer/address/zipCode");
	}
	
	public String getAddressCountry(){
		return getRequestNodeValueByXPath("/Envelope/Body/create/customer/address/country");
	}
	
	public String getAddressCompanyName(){
		return getRequestNodeValueByXPath("/Envelope/Body/create/customer/address/companyName");
	}
	
	/*
	 * Customer Identifier
	 */
	public String getId(){
		return getRequestNodeValueByXPath("/Envelope/Body/create/customer/id");
	}
}
