package com.orasi.api.SoapExamples.predict8.IDServiceBinding.operations;

import com.orasi.api.SoapExamples.predict8.IDServiceBinding.IDServiceBinding;
import com.orasi.utils.XMLTools;

public class Generate extends IDServiceBinding{
	String generateValue = "?";
	public Generate(String environment, String scenario) {
		super(environment);
		
		//Generate a request from a project xml file
		setRequestDocument(XMLTools.loadXML(buildRequestFromWSDL("generate")));
		System.out.println(getRequest());
				
		removeComments() ;
		removeWhiteSpace();
	}
	
	public void updateGenerateValue(String value){
		generateValue = value;
		setRequestNodeValueByXPath("/Envelope/Body/generate", value);
	}
	
	public String getGenerateValue(){
		return generateValue;
	}
	
	public String getResponseId(){
		return getResponseNodeValueByXPath("/Envelope/Body/generateResponse/id");
	}
}
