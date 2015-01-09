package com.orasi.api.SoapExamples.predict8.ArticleServicePTBinding;

import com.orasi.api.soapServices.core.*;

public class ArticleServicePTBinding extends SoapService{
	public ArticleServicePTBinding(String environment) {	
		setEnvironmentServiceURL("http://www.predic8.com:8080/material/ArticleService");	
	}
}
