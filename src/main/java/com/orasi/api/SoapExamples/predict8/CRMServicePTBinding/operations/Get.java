package com.orasi.api.SoapExamples.predict8.CRMServicePTBinding.operations;

import com.orasi.api.SoapExamples.predict8.CRMServicePTBinding.CRMServicePTBinding;
import com.orasi.utils.XMLTools;

public class Get extends CRMServicePTBinding{
	public Get(String environment, String scenario) {
		super(environment);
		
		//Generate a request from a project xml file
		setRequestDocument(XMLTools.loadXML(buildRequestFromWSDL("get")));
		System.out.println(getRequest());
				
		removeComments() ;
		removeWhiteSpace();
	}
	
	/*
	 * Customer Personal Data
	 */
	public void setRequestId(String value){
		setRequestNodeValueByXPath("/Envelope/Body/get/id", value);
	}
	
	public String getResponsePersonId(){
		return getResponseNodeValueByXPath("/Envelope/Body/getResponse/customer/person/id");
	}
	
	public String getResponsePersonFirstName(){
		return getResponseNodeValueByXPath("/Envelope/Body/getResponse/customer/person/firstName");
	}
	
	public String getResponsePersonLastName(){
		return getResponseNodeValueByXPath("/Envelope/Body/getResponse/customer/person/lastName");
	}
	
	public String getResponsePersonAddressStreet(){
		return getResponseNodeValueByXPath("/Envelope/Body/getResponse/customer/person/address/street");
	}
	
	public String getResponsePersonAddressCity(){
		return getResponseNodeValueByXPath("/Envelope/Body/getResponse/customer/person/address/city");
	}
	
	public String getResponsePersonAddressZipCode(){
		return getResponseNodeValueByXPath("/Envelope/Body/getResponse/customer/person/address/zipCode");
	}
	
	public String getResponsePersonAddressCountry(){
		return getResponseNodeValueByXPath("/Envelope/Body/getResponse/customer/person/address/country");
	}
	
	public String getResponsePersonAge(){
		return getResponseNodeValueByXPath("/Envelope/Body/getResponse/customer/person/age");
	}
	
	/*
	 * Customer Address Data
	 */
	public String getResponseAddressStreet(){
		return getResponseNodeValueByXPath("/Envelope/Body/getResponse/customer/address/street");
	}
	
	public String getResponseAddressCity(){
		return getResponseNodeValueByXPath("/Envelope/Body/getResponse/customer/address/city");
	}
	
	public String getResponseAddressZipCode(){
		return getResponseNodeValueByXPath("/Envelope/Body/getResponse/customer/address/zipCode");
	}
	
	public String getResponseAddressCountry(){
		return getResponseNodeValueByXPath("/Envelope/Body/getResponse/customer/address/country");
	}
	
	public String getResponseAddressCompanyName(){
		return getResponseNodeValueByXPath("/Envelope/Body/getResponse/customer/address/companyName");
	}
	
	/*
	 * Customer Identifier
	 */
	public String getResponseId(){
		return getResponseNodeValueByXPath("/Envelope/Body/getResponse/customer/id");
	}
}
