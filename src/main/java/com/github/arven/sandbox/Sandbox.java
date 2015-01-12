/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.sandbox;

import com.github.arven.text.MapMessageFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author brian.becker
 */
public class Sandbox {
    public static void main(String[] args) throws Exception {
        System.out.println("Testing");
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
        //fmt.parse
    }
}
