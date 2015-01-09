package com.orasi.api.SoapExamples.predict8.ArticleServicePTBinding.operations;

import com.orasi.api.SoapExamples.predict8.ArticleServicePTBinding.ArticleServicePTBinding;
import com.orasi.utils.XMLTools;

public class Create extends ArticleServicePTBinding{
	public Create(String environment, String scenario) {
		super(environment);
		
		//Generate a request from a project xml file
		setRequestDocument(XMLTools.loadXML(buildRequestFromWSDL("create")));
		System.out.println(getRequest());
				
		removeComments() ;
		removeWhiteSpace();
	}
	
	public void setArticleName(String value){
		setRequestNodeValueByXPath("/Envelope/Body/create/article/name", value);
	}
	
	public void setArticleDescription(String value){
		setRequestNodeValueByXPath("/Envelope/Body/create/article/description", value);
	}
	
	public void setArticlePriceAmount(String value){
		setRequestNodeValueByXPath("/Envelope/Body/create/article/price/amount", value);
	}
	
	public void setArticlePriceCurrency(String value){
		setRequestNodeValueByXPath("/Envelope/Body/create/article/price/currency", value);
	}
	
	public void setArticleId(String value){
		setRequestNodeValueByXPath("/Envelope/Body/create/article/id", value);
	}
	
	public String getResponseArticleId(){
		return getResponseNodeValueByXPath("/Envelope/Body/createResponse/id");
	}
}
