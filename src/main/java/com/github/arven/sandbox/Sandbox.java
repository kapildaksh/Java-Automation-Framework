/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.sandbox;

import com.github.arven.text.MapMessageFormat;
import com.github.arven.text.TemplateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author brian.becker
 */
public class Sandbox {
    public static void main(String[] args) throws Exception {
        /*System.out.println("Testing");
        System.out.println(MessageFormat.format("I''m a {0} and I''m {1}, the answer is {2,number,integer}.", "MessageFormat", "Integer Keyed", 42));
        //System.out.println(MapMessageFormat.format("I''m a {0} and I''m {1}.", "String", "Defined"));
        Map map = new HashMap();
        map.put("type", "MapMessageFormat");
        map.put("keyed", "String keyed");
        map.put("answer", 42);
        
        MapMessageFormat fmt = new MapMessageFormat("I''m a {answer,number,integer} and I''m {keyed}, the answer is {type}. The answer to life, the universe, and everything is {answer,number,integer}.");
        String toParse = fmt.format(map);
        System.out.println(toParse);
        Map parsed = fmt.parse(toParse);
        System.out.println(parsed);
        
        System.out.println(Arrays.asList(fmt.getFormatsByArgumentName()));
        */
        Map names = new HashMap();
        names.put("tst", "VxTesting 192");
        names.put("arv", "( Brian Becker )");
        names.put("trf", "( T. R. Fields )");
        names.put("prs", "P. R. Skylar");
        names.put("xyz", "Xyzzy");
        names.put("0", 500);
        MapMessageFormat tf = new MapMessageFormat("This is a test, arv. Now we will use real variables. {arv}zx, {trf}st, ys{prs}li: all{xyz} {0}");
        String formatted = tf.format(names);
        System.out.println(formatted);
        Map parsed = (Map) tf.parseObject(formatted);
        System.out.println(parsed);
        
        TemplateFormat template = new TemplateFormat("${tst}This is a test, $${arv}. Now we will use real variables. ${arv,numeric}zx, ${trf}st, ys${prs}li: all${xyz} ${0}");
        String f2 = template.format(names);
        System.out.println(f2);
        Map p2 = (Map) template.parseObject(f2);
        System.out.println(p2);
        //String formatted = tf.format(names);
        //System.out.println(formatted);
        //Object o = tf.parseObject(formatted);
        //System.out.println(o.toString());
        
        //Map m = new HashMap();
        //m.put("arv", "-- Brian Becker");
        //System.out.println(tf.format(new HashMap()));
        
        //fmt.parse
    }
}
