package com.orasi.api.SoapExamples.predict8.ArticleServicePTBinding.operations;

import com.orasi.api.SoapExamples.predict8.ArticleServicePTBinding.ArticleServicePTBinding;
import com.orasi.utils.XMLTools;

public class Delete extends ArticleServicePTBinding{
	public Delete(String environment, String scenario) {
		super(environment);
		
		//Generate a request from a project xml file
		setRequestDocument(XMLTools.loadXML(buildRequestFromWSDL("getAll")));
		System.out.println(getRequest());
				
		removeComments() ;
		removeWhiteSpace();
	}

}
