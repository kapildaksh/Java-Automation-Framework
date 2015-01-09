package com.orasi.api.SoapExamples.predict8.ArticleServicePTBinding.operations;

import com.orasi.api.SoapExamples.predict8.ArticleServicePTBinding.ArticleServicePTBinding;
import com.orasi.utils.XMLTools;

public class Get extends ArticleServicePTBinding{
	public Get(String environment, String scenario) {
		super(environment);
		
		//Generate a request from a project xml file
		setRequestDocument(XMLTools.loadXML(buildRequestFromWSDL("get")));
		System.out.println(getRequest());
				
		removeComments() ;
		removeWhiteSpace();
	}
	
	public void setRequestId(String value){
		setRequestNodeValueByXPath("/Envelope/Body/get/id", value);
	}

	public String getResponseName(){
		return getResponseNodeValueByXPath("/Envelope/Body/getResponse/article/name");
	}

	public String getResponseDescription(){
		return getResponseNodeValueByXPath("/Envelope/Body/getResponse/article/description");
	}

	public String getResponsePriceAmount(){
		return getResponseNodeValueByXPath("/Envelope/Body/getResponse/article/price/amount");
	}

	public String getResponsePriceCurrency(){
		return getResponseNodeValueByXPath("/Envelope/Body/getResponse/article/price/currency");
	}

	public String getResponseId(){
		return getResponseNodeValueByXPath("/Envelope/Body/getResponse/article/id");
	}
}
