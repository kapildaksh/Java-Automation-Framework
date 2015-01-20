package com.orasi.sandbox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import com.orasi.utils.dataProviders.JSONDataProvider;

public class JSONTest {
    @Test()
    public void main() throws JSONException{
        String str = "[{\"nameColumn\":\"name1\",\"urlColumn\":\"url1\"},{\"nameColumn\":\"name2\",\"urlColumn\":\"url2\"},{\"nameColumn\":\"name3\",\"urlColumn\":\"url3\"}]";
        JSONArray jsonarray = new JSONArray(str);
        Object [][] test = JSONDataProvider.compileJSON("", str);
    
        for(int i=0; i<jsonarray.length(); i++){
            JSONObject obj = jsonarray.getJSONObject(i);
    
            String name = obj.getString("nameColumn");
            String url = obj.getString("urlColumn");
    
            System.out.println(name);
            System.out.println(url);
        }
    }
}
