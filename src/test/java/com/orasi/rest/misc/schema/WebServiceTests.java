/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.rest.misc.schema;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.orasi.text.MapMessageFormat;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.library.DraftV4Library;
import com.github.fge.jsonschema.library.Keyword;
import com.github.fge.jsonschema.library.Library;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.messages.JsonSchemaValidationBundle;
import com.github.fge.msgsimple.bundle.MessageBundle;
import com.github.fge.msgsimple.load.MessageBundles;
import com.github.fge.msgsimple.source.MapMessageSource;
import com.github.fge.msgsimple.source.MessageSource;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Map;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 *
 * @author Brian Becker
 */
public class WebServiceTests extends RestDebuggerTest {
    
    public static final String REST_SANDBOX = "/rest/sandbox/";
    
    public final ObjectMapper mapper;
    
    public WebServiceTests() {
        keepRunning = true;
        mapper = new ObjectMapper();
    }
    
    @Test
    public void fstabSchema() throws Exception {
        JsonSchema schema = JsonSchemaFactory.byDefault().getJsonSchema(WebServiceTests.class.getResource(REST_SANDBOX + "schema.json").toString());
        JsonSchema entry = JsonSchemaFactory.byDefault().getJsonSchema(WebServiceTests.class.getResource(REST_SANDBOX + "entry-schema.json").toString());
        JsonNode node = mapper.readTree(WebServiceTests.class.getResource(REST_SANDBOX + "fstab.json"));
        ProcessingReport report = schema.validate(node);
        println("Schema Valid? " + yesno(report.isSuccess()));
        Assert.assertTrue(report.isSuccess());
    }
    
    @Test
    public void addCustomValidatorBad() throws Exception {
        final Library library = DraftV4Library.get().thaw()
            .addFormatAttribute("device-path", PathAttributeBad.getInstance()).freeze();
        
        final String key = "invalidPath";
        final String value = "input is not a valid device path";
        final MessageSource source = MapMessageSource.newBuilder().put(key, value).build();
        final MessageBundle bundle = MessageBundles.getBundle(JsonSchemaValidationBundle.class).thaw().appendSource(source).freeze();

        final ValidationConfiguration cfg = ValidationConfiguration.newBuilder()
            .setDefaultLibrary("http://my.site/myschema#", library)
            .setValidationMessages(bundle).freeze();
        final JsonSchemaFactory factory = JsonSchemaFactory.newBuilder()
            .setValidationConfiguration(cfg).freeze();
        
        JsonSchema schema = factory.getJsonSchema(WebServiceTests.class.getResource(REST_SANDBOX + "schema.json").toString());
        JsonSchema entry = factory.getJsonSchema(WebServiceTests.class.getResource(REST_SANDBOX + "entry-schema.json").toString());
        JsonNode node = mapper.readTree(WebServiceTests.class.getResource(REST_SANDBOX + "fstab.json"));
        ProcessingReport report = schema.validate(node);
       
        println("Schema Valid? " + yesno(report.isSuccess()));
        Assert.assertFalse(report.isSuccess());
    }
    
    @Test
    public void addCustomValidatorGood() throws Exception {
        final Library library = DraftV4Library.get().thaw()
            .addFormatAttribute("device-path", PathAttributeGood.getInstance()).freeze();
               
        final String key = "invalidPath";
        final String value = "input is not a valid device path";
        final MessageSource source = MapMessageSource.newBuilder().put(key, value).build();
        final MessageBundle bundle = MessageBundles.getBundle(JsonSchemaValidationBundle.class).thaw().appendSource(source).freeze();

        final ValidationConfiguration cfg = ValidationConfiguration.newBuilder()
            .setDefaultLibrary("http://my.site/myschema#", library)
            .setValidationMessages(bundle).freeze();
        final JsonSchemaFactory factory = JsonSchemaFactory.newBuilder()
            .setValidationConfiguration(cfg).freeze();
        
        JsonSchema schema = factory.getJsonSchema(WebServiceTests.class.getResource(REST_SANDBOX + "schema.json").toString());
        JsonSchema entry = factory.getJsonSchema(WebServiceTests.class.getResource(REST_SANDBOX + "entry-schema.json").toString());
        JsonNode node = mapper.readTree(WebServiceTests.class.getResource(REST_SANDBOX + "fstab.json"));
        ProcessingReport report = schema.validate(node);
       
        println("Schema Valid? " + yesno(report.isSuccess()));
        Assert.assertTrue(report.isSuccess());
    }
    
    @Test
    public void addCustomKeywordBad() throws Exception {
        final Library library = DraftV4Library.get().thaw()
            .addKeyword(
                    Keyword.newBuilder("x-in-alphabetical-order")
                            .withValidatorClass(AlphabeticalOrderKeywordBad.class)
                            .withSyntaxChecker(AlphabeticalOrderSyntaxChecker.getInstance())
                            .withDigester(AlphabeticalOrderDigester.getInstance()).freeze()).freeze();
               
        final String key = "invalidPath";
        final String value = "input is not a valid device path";
        final MessageSource source = MapMessageSource.newBuilder().put(key, value).build();
        final MessageBundle bundle = MessageBundles.getBundle(JsonSchemaValidationBundle.class).thaw().appendSource(source).freeze();

        final ValidationConfiguration cfg = ValidationConfiguration.newBuilder()
            .setDefaultLibrary("http://my.site/myschema#", library)
            .setValidationMessages(bundle).freeze();
        final JsonSchemaFactory factory = JsonSchemaFactory.newBuilder()
            .setValidationConfiguration(cfg).freeze();
        
        JsonSchema schema = factory.getJsonSchema(WebServiceTests.class.getResource(REST_SANDBOX + "schema.json").toString());
        JsonSchema entry = factory.getJsonSchema(WebServiceTests.class.getResource(REST_SANDBOX + "entry-schema.json").toString());
        JsonNode node = mapper.readTree(WebServiceTests.class.getResource(REST_SANDBOX + "fstab.json"));
        ProcessingReport report = schema.validate(node);
       
        println("Schema Valid? " + yesno(report.isSuccess()));
        Assert.assertTrue(report.isSuccess());
    }
    
    @Test
    public void addCustomKeywordGood() throws Exception {
        final Library library = DraftV4Library.get().thaw()
            .addKeyword(
                    Keyword.newBuilder("x-in-alphabetical-order")
                            .withValidatorClass(AlphabeticalOrderKeywordGood.class)
                            .withSyntaxChecker(AlphabeticalOrderSyntaxChecker.getInstance())
                            .withDigester(AlphabeticalOrderDigester.getInstance()).freeze()).freeze();
               
        final String key = "invalidPath";
        final String value = "input is not a valid device path";
        final MessageSource source = MapMessageSource.newBuilder().put(key, value).build();
        final MessageBundle bundle = MessageBundles.getBundle(JsonSchemaValidationBundle.class).thaw().appendSource(source).freeze();

        final ValidationConfiguration cfg = ValidationConfiguration.newBuilder()
            .setDefaultLibrary("http://my.site/myschema#", library)
            .setValidationMessages(bundle).freeze();
        final JsonSchemaFactory factory = JsonSchemaFactory.newBuilder()
            .setValidationConfiguration(cfg).freeze();
        
        JsonSchema schema = factory.getJsonSchema(WebServiceTests.class.getResource(REST_SANDBOX + "schema.json").toString());
        JsonSchema entry = factory.getJsonSchema(WebServiceTests.class.getResource(REST_SANDBOX + "entry-schema.json").toString());
        JsonNode node = mapper.readTree(WebServiceTests.class.getResource(REST_SANDBOX + "fstab.json"));
        ProcessingReport report = schema.validate(node);
       
        println("Schema Valid? " + yesno(report.isSuccess()));
        Assert.assertTrue(report.isSuccess());
    }
    
    @Test
    public void basicTestFromFile() throws Exception {
        JsonNode node = mapper.readTree(new URL("file:///C:/Users/brian.becker/Git/java-rest-schema/target/test-classes/fstab.json"));
        Assert.assertTrue(node.getNodeType() == JsonNodeType.OBJECT);
        Assert.assertTrue(node.path("/tmp").path("storage").path("sizeInMB").isNumber());
        Assert.assertEquals(node.path("/tmp").path("storage").path("sizeInMB").asLong(), 64);
        
        Assert.assertTrue(node.path("/var/www").path("storage").path("server").isValueNode());
        Assert.assertEquals(node.path("/var/www").path("storage").path("server").asText(), "my.nfs.server");
    }
    
    @Test
    public void atSelectFromFile() throws Exception {
        JsonNode node = mapper.readTree(new URL("file:///C:/Users/brian.becker/Git/java-rest-schema/target/test-classes/fstab.json"));
        Assert.assertEquals(node.at("/~1var/options/0").asText(), "nosuid");
        Assert.assertEquals(node.at(JSONP.slash("/var storage label")).asText(), "8f3ba6f4-5c70-46ec-83af-0d5434953e5f");
    }
    
    @Test
    public void regularMessageFormat() throws Exception {
        JsonNode node = mapper.readTree(new URL("file:///C:/Users/brian.becker/Git/java-rest-schema/target/test-classes/errorpage.json"));
        String error = node.get("error").asText();
        MessageFormat format = new MessageFormat("{0} {1}: The server was unable to find the {3} that was requested from Article {2}");
        Object[] parsed = format.parse(error);
        println(Arrays.asList(parsed));
        Assert.assertEquals(parsed[0], "404");
        Assert.assertEquals(parsed[1], "Not Found");
        Assert.assertEquals(parsed[2], "123");
        Assert.assertEquals(parsed[3], "Comment resource");
    }

    @Test
    public void mapMessageFormat() throws Exception {
        JsonNode node = mapper.readTree(new URL("file:///C:/Users/brian.becker/Git/java-rest-schema/target/test-classes/errorpage.json"));
        String error = node.get("error").asText();
        MapMessageFormat format = new MapMessageFormat("{errorCode} {errorName}: The server was unable to find the {extraType} that was requested from Article {articleNo}");
        Map map = format.parse(error);
        Assert.assertEquals(map.get("errorCode"), "404");
        Assert.assertEquals(map.get("errorName"), "Not Found");
        Assert.assertEquals(map.get("articleNo"), "123");
        Assert.assertEquals(map.get("extraType"), "Comment resource");
    }
    
    //@Test
    //public void fails2() throws Exception {
        // JsonSchema entry = JsonSchemaFactory.byDefault().getJsonSchema(WebServiceTests.class.getResource("entry-schema.json").toString());
    //}
    
}
