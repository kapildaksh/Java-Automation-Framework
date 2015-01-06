package com.orasi.api.restServices.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import com.orasi.utils.documentConverter.StringToDocumentToString;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.orasi.utils.XMLTools;

public class RestService {
	private String defaultResponseFormat = "json";
	private static Document xmlResponseDocument = null;

	private boolean valiateAcceptableFormat(String format) {
		if (format == "xml" || format == "json")
			return true;
		return false;
	}

	public void setDefaultResponseFormat(String format) {
		if (valiateAcceptableFormat(format)) {
			this.defaultResponseFormat = format.toLowerCase();
		} else {
			throw new RuntimeException("Invalid response format entered. Acceptable formats are 'json' or 'xml'");
		}
	}

	public String getDefaultResponseFormat() {
		return defaultResponseFormat;
	}

	public String sendGetRequest(String url) throws IOException {
		return sendGetRequest(url, getDefaultResponseFormat());
	}

	public String sendGetRequest(String url, String responseFormat){

		StringBuilder rawResponse = new StringBuilder();

		//Replace any white space with '%20'
		url = url.replaceAll(" ", "%20");
		
		HttpURLConnection conn = httpConnectionBuilder(url, "GET",
				responseFormat);

		InputStream stream = null;
		String buffer = "";

		try {
			stream = conn.getInputStream();
			BufferedReader bufferReader = new BufferedReader(
					new InputStreamReader(stream));
			while ((buffer = bufferReader.readLine()) != null) {
				rawResponse.append(buffer);
			}
		} catch (IOException ioe) {
			throw new RuntimeException(ioe.getMessage());
		}
		
		if(responseFormat.equalsIgnoreCase("xml")){
			xmlResponseDocument = StringToDocumentToString.convertStringToDocument(rawResponse.toString());
			xmlResponseDocument.normalize();
			System.out.println();
			System.out.println();
			System.out.println("Response");
			System.out.println(getResponse());
		}
		
		return rawResponse.toString();
	}

	private URL urlBuilder(String url) {

		URL urlRequest = null;

		try {
			urlRequest = new URL(url);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return urlRequest;
	}

	private HttpURLConnection httpConnectionBuilder(String url,
			String requestMethod, String responseFormat) {
		HttpURLConnection connection = null;
		URL urlRequest = urlBuilder(url);

		try {
			connection = (HttpURLConnection) urlRequest.openConnection();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connection.setDoOutput(true);
		connection.setDoInput(true);
		if (valiateAcceptableFormat(responseFormat))
			connection.setRequestProperty("Accept", "application/"
					+ responseFormat);

		try {
			connection.setRequestMethod(requestMethod.toUpperCase());
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connection;
	}
	
	public String getXmlResponseByXpath(String xpath){
		return XMLTools.getValueByXpath(getXmlResponseDocument(), xpath);
	}
	
	/**
	 * @summary This is used to retrieve the current Response Document as it is
	 *          in memory
	 * @precondition The Response Document needs to be set by
	 *               {@link #setResponseDocument(Document)}
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @return Returns the stored Response XML as a Document object
	 */
	protected static Document getXmlResponseDocument() {
		return xmlResponseDocument;
	}
	
	/**
	 * @summary Takes the current Response XML Document stored in memory and
	 *          return it as a string for simple output
	 * @precondition Requires XML Document to be loaded by using
	 *               {@link #setResponseDocument}
	 * @author Justin Phlegar
	 * @version Created 08/28/2014
	 * @return Will return the current Response XML as a string
	 */
	public String getResponse() {
		StringWriter sw = new StringWriter();
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = null;
		try {
			transformer = tf.newTransformer();
		} catch (TransformerConfigurationException e) {
			throw new RuntimeException("Failed to create XML Transformer");
		}
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

		try {
			transformer.transform(new DOMSource(getXmlResponseDocument()),
					new StreamResult(sw));
		} catch (TransformerException e) {
			throw new RuntimeException(
					"Failed to transform Response XML Document. Ensure XML Document has been successfully loaded.");
		}
		return sw.toString();
	}
	
	public int getNumberOfNodesByXpath(String path) throws XPathExpressionException{
	    //creating an XPathFactory:
	    XPathFactory factory = XPathFactory.newInstance();
	    //using this factory to create an XPath object: 
	    XPath xpath = factory.newXPath();

	    // XPath Query for showing all nodes value
	    XPathExpression expr = xpath.compile(path);
	    Object result = expr.evaluate(this.xmlResponseDocument, XPathConstants.NODESET);
	    NodeList nodes = (NodeList) result;
	    
	    return nodes.getLength();
	}
}
