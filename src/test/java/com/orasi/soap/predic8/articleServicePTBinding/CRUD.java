package com.orasi.soap.predic8.articleServicePTBinding;

import javax.xml.bind.ValidationException;
import javax.xml.xpath.XPathExpressionException;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.orasi.api.SoapExamples.predict8.ArticleServicePTBinding.operations.Create;
import com.orasi.api.SoapExamples.predict8.ArticleServicePTBinding.operations.Delete;
import com.orasi.api.SoapExamples.predict8.ArticleServicePTBinding.operations.Get;
import com.orasi.api.SoapExamples.predict8.ArticleServicePTBinding.operations.GetAll;
import com.orasi.utils.Constants;
import com.orasi.utils.dataProviders.ExcelDataProvider;

public class CRUD {
	String id;
	String name = "My Article";
	String description = "My Article Description";
	String priceAmount = "1.5";
	String priceCurrency = "USD";
	
	@DataProvider(name = "dataScenario")
	public Object[][] scenarios() {
		return new ExcelDataProvider(Constants.PREDIC8API_DATAPROVIDER_PATH
				+ "articleServicePTBinding_CRUD.xlsx", "CRUD").getTestData();
	}
	
	@Test(dataProvider = "dataScenario", groups = { "soap" })
	public void main(String testScenario, String name, String description, 
			String priceAmount, String priceCurrency
			) throws XPathExpressionException, ValidationException{		
		GetAll all = new GetAll("stage","Main");
		all.sendRequest();
		Assert.assertEquals(all.getResponseStatusCode(), "200");
		
		Create create = new Create("stage","Main");
		create.setArticleName(name);
		create.setArticleDescription(description);
		create.setArticlePriceAmount(priceAmount);
		create.setArticlePriceCurrency(priceCurrency);
		create.setArticleId("1");
		create.sendRequest();
		Assert.assertEquals(create.getResponseStatusCode(), "200");
		id = create.getResponseArticleId();
		String[] attributes = {name, description, priceAmount, priceCurrency, id};
		
		Get get = new Get("stage","Main");
		get.setRequestId(id);
		get.sendRequest();
		Assert.assertEquals(get.getResponseStatusCode(), "200");
		Assert.assertEquals(get.getResponseName(), name, "The article name in the response ["+get.getResponseName()+"] was not that which was expected ["+name+"].");
		Assert.assertEquals(get.getResponseDescription(), description, "The article description in the response ["+get.getResponseDescription()+"] was not that which was expected ["+description+"].");
		Assert.assertEquals(get.getResponsePriceAmount(), priceAmount, "The article price amount in the response ["+get.getResponsePriceAmount()+"] was not that which was expected ["+priceAmount+"].");
		Assert.assertEquals(get.getResponsePriceCurrency(), priceCurrency, "The article price currency in the response ["+get.getResponsePriceCurrency()+"] was not that which was expected ["+priceCurrency+"].");
		Assert.assertEquals(get.getResponseId(), id, "The article id in the response ["+get.getResponseId()+"] was not that which was expected ["+id+"].");
		
		GetAll getAll = new GetAll("stage","Main");
		getAll.sendRequest();
		Assert.assertEquals(getAll.getResponseStatusCode(), "200");
		getAll.verifyPresenceOfArticle(attributes, "/Envelope/Body/getAllResponse/article", true);
		
		Delete delete = new Delete("stage","Main");
		delete.setRequestId(id);
		delete.sendRequest();
		Assert.assertEquals(delete.getResponseStatusCode(), "200");
		
		getAll = new GetAll("stage","Main");
		getAll.sendRequest();
		Assert.assertEquals(getAll.getResponseStatusCode(), "200");
		getAll.verifyPresenceOfArticle(attributes, "/Envelope/Body/getAllResponse/article", false);
	}
}
