/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.sleuth.commands;

import bsh.CallStack;
import bsh.Interpreter;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author brian.becker
 */
public class json {
    public static Object invoke(Interpreter env, CallStack callstack, String s) throws Exception {
        ObjectMapper m = new ObjectMapper();
        return m.readValue(s, Object.class);
    }
    
    public static Object json(String s) throws Exception {
        ObjectMapper m = new ObjectMapper();
        return m.readValue(s, Object.class);
    }
}
