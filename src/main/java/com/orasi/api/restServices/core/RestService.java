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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;




import com.orasi.utils.XMLTools;

public class RestService {
	private String defaultResponseFormat = "json";
	private static Document xmlResponseDocument = null;
	private static String jsonResponseString = null;
	private static JSONObject jsonResponseObject = null;

	/**
	 * @summary This is used to determine is the user-defined format is one that
	 *          is acceptable for use with REST services
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @param format
	 *            - string; used to determine the format of the response
	 * @return true if the format is accepteable, false otherwise
	 */
	private boolean valiateAcceptableFormat(String format) {
		if (format.equalsIgnoreCase("xml") || format.equalsIgnoreCase("json"))
			return true;
		return false;
	}

	/**
	 * @summary This is used to set the format
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @param format
	 *            - string; used to determine the format of the response
	 */
	public void setDefaultResponseFormat(String format) {
		if (valiateAcceptableFormat(format)) {
			this.defaultResponseFormat = format.toLowerCase();
		} else {
			throw new RuntimeException(
					"Invalid response format entered. Acceptable formats are 'json' or 'xml'");
		}
	}

	/**
	 * @summary This is used to retrieve the current default response format
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @return Returns the response format as a string
	 */
	public String getDefaultResponseFormat() {
		return defaultResponseFormat;
	}

	/**
	 * @summary This is used to invoke the REST "Get" request
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @param url
	 *            - endpoint for the REST service
	 * @return Returns the response to the "Get" request as a string
	 * @throws JSONException
	 */
	public String sendGetRequest(String url) throws IOException, JSONException {
		return sendGetRequest(url, getDefaultResponseFormat());
	}

	/**
	 * @summary This is used to invoke the REST "Get" request and generate a
	 *          string version of the response If the default format is XML, an
	 *          XML document is generated for later use
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @param url
	 *            - endpoint for the REST service
	 * @return Returns the response to the "Get" request as a string
	 * @throws JSONException
	 */
	public String sendGetRequest(String url, String responseFormat)
			throws JSONException {
		System.out.println("REST endpoint: " + url);

		StringBuilder rawResponse = new StringBuilder();

		// Replace any white space in the URL with '%20'
		url = url.replaceAll(" ", "%20");

		// Build the connection string
		HttpURLConnection conn = httpConnectionBuilder(url, "GET",
				responseFormat);

		InputStream stream = null;
		String buffer = "";

		// Invoke the REST service and, given there are no errors, read the
		// response into a string format
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

		if (responseFormat.equalsIgnoreCase("xml")) {
			setXmlResponseDocument(StringToDocumentToString
					.convertStringToDocument(rawResponse.toString()));

			System.out.println();
			System.out.println("Raw XML Response");
			System.out.println(getXmlResponse());
		} else if (responseFormat.equalsIgnoreCase("json")) {
			System.out.println();
			System.out.println("Raw JSON Response");
			System.out.println(rawResponse.toString());

			setJsonResponseObject(new JSONObject(rawResponse.toString()));
			setJsonResponseString(rawResponse.toString());
		}

		return rawResponse.toString();
	}

	/**
	 * @summary This is used to build the REST URL
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @param url
	 *            - endpoint for the REST service
	 * @return Returns the url as a URL object
	 */
	private URL urlBuilder(String url) {

		URL urlRequest = null;

		try {
			urlRequest = new URL(url);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		return urlRequest;
	}

	/**
	 * @summary This is used to build the HTTP URL connection
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @param url
	 *            - endpoint for the REST service
	 * @param requestMethod
	 *            - REST method to be invoked (e.g. "Get", "Put", etc)
	 * @param responseFormat
	 *            - response format expected from the REST service (e.g. "xml"
	 *            or "json")
	 * @return Returns a HttpURLConnection object
	 */
	private HttpURLConnection httpConnectionBuilder(String url,
			String requestMethod, String responseFormat) {
		HttpURLConnection connection = null;
		URL urlRequest = urlBuilder(url);

		try {
			connection = (HttpURLConnection) urlRequest.openConnection();
		} catch (IOException e) {
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
			e.printStackTrace();
		}
		return connection;
	}

	/**
	 * @summary Set a Response XML Document to be stored in memory to be
	 *          retrieved and edited easily. Retrieve XML Document using
	 *          {@link #getXmlResponseDocument()} or as a String using
	 *          {@link #getXmlResponse()}
	 * @precondition Requires valid XML Document to be sent
	 * @author Justin Phlegar
	 * @version Created 08/28/2014
	 * @param doc
	 *            Document: XML file of the Response to be stored in memory
	 */
	protected void setXmlResponseDocument(Document doc) {
		xmlResponseDocument = doc;
		xmlResponseDocument.normalize();
	}

	/**
	 * @summary This is used to find the value of an XML node using xpath
	 * @author Justin Phlegar
	 * @version Created: 08/28/2014
	 * @precondition The Response Document needs to be set by
	 *               {@link #setXMLResponseDocument(Document)}
	 * @param xpath
	 *            - path of the node from which to pull the value
	 * @return string representation of the node value
	 */
	public String getXmlResponseByXpath(String xpath) {
		return XMLTools.getValueByXpath(getXmlResponseDocument(), xpath);
	}

	/**
	 * @summary This is used to retrieve the current Response Document as it is
	 *          in memory
	 * @precondition The Response Document needs to be set by
	 *               {@link #setXmlResponseDocument(Document)}
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
	public String getXmlResponse() {
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

	/**
	 * @summary Returns the number of nodes for a given xpath. Useful for
	 *          determining if indexing is need to access multiple sibling nodes
	 * @precondition Requires XML Document to be loaded by using
	 *               {@link #setResponseDocument}
	 * @author Waightstill W. Avery
	 * @version Created 01/06/2015
	 * @param path
	 *            - string, xpath
	 * @return integer, number of nodes found with the given xpath
	 */
	@SuppressWarnings("static-access")
	public int getNumberOfNodesByXpath(String path)
			throws XPathExpressionException {
		// creating an XPathFactory:
		XPathFactory factory = XPathFactory.newInstance();
		// using this factory to create an XPath object:
		XPath xpath = factory.newXPath();

		// XPath Query for showing all nodes value
		XPathExpression expr = xpath.compile(path);
		Object result = expr.evaluate(this.xmlResponseDocument,
				XPathConstants.NODESET);
		NodeList nodes = (NodeList) result;

		return nodes.getLength();
	}

	/**
	 * @summary Returns the number of nodes for a given xpath. Useful for
	 *          determining if indexing is need to access multiple sibling nodes
	 * @precondition Requires XML Document to be loaded by using
	 *               {@link #setResponseDocument}
	 * @author Waightstill W. Avery
	 * @version Created 01/06/2015
	 * @param path
	 *            - string, xpath
	 * @return integer, number of nodes found with the given xpath
	 */
	@SuppressWarnings("static-access")
	public int getNumberOfChildNodesByXpath(String path)
			throws XPathExpressionException {
		// creating an XPathFactory:
		XPathFactory factory = XPathFactory.newInstance();
		// using this factory to create an XPath object:
		XPath xpath = factory.newXPath();

		// XPath Query for showing all nodes value
		XPathExpression expr = xpath.compile(path);
		Object result = expr.evaluate(this.xmlResponseDocument,
				XPathConstants.NODESET);
		NodeList nodes = (NodeList) result;

		// Uncomment below code to output child node values to the console
		/*
		 * System.out.println(); for(int nodesList = 0; nodesList <
		 * nodes.item(0).getChildNodes().getLength(); nodesList++){
		 * System.out.println
		 * (nodes.item(0).getChildNodes().item(nodesList).getNodeName()); }
		 */

		return (int) nodes.item(0).getChildNodes().getLength() / 2;
	}

	public String getJsonResponseValueByKeyString(String keyString) throws JSONException {
		String[] jsonObjects = keyString.split(";");
		JSONObject jo = new JSONObject(getJsonResponseString());
		JSONArray ja = new JSONArray();
		String value = "";
		String path = "";
		
		for(String keyCounter: jsonObjects){
			String[] keyParts = keyCounter.split(",");
			switch (keyParts[1].toLowerCase()) {
			case "object":case "jsonobject":
				Assert.assertEquals(keyParts.length, 2, "Two parts are needed for a JSONObject to be retrieved: [keyName,object]");
				jo = jo.getJSONObject(keyParts[0]);
				break;
			case "array":case "jsonarray":
				Assert.assertEquals(keyParts.length, 3, "Three parts are needed for a JSONObject to be retrieved: [keyName,object,arrayIndex]");
				ja = jo.getJSONArray(keyParts[0]);
				jo = (JSONObject) ja.getJSONObject(Integer.parseInt(keyParts[2]));
				break;
			case "string":
				value = jo.get(keyParts[0]).toString();
				break;
			default:
				break;
			}
			path += keyParts[0] + ";";
		}
		//Uncomment below code to output the json 'path' and value to the console
		//System.out.println("Value for the path ["+path +"] = "+ value);
		return value;
	}

	private void setJsonResponseString(String response) {
		jsonResponseString = response;
	}

	public String getJsonResponseString() {
		return jsonResponseString;
	}
	
	private void setJsonResponseObject(JSONObject response) {
		jsonResponseObject = response;
	}

	public JSONObject getJsonResponseObject() {
		return jsonResponseObject;
	}
}
