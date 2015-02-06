/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.rest.microblogAPI.schema;

/**
 *
 * @author brian.becker
 */
public class JSONP {
    public static String slash(String esc) {
        StringBuilder sb = new StringBuilder();
        sb.append("/");
        for(int i = 0; i < esc.length(); i++) {
            if(esc.charAt(i) == '/') {
                sb.append("~1");
            } else if(esc.charAt(i) == '~') {
                sb.append("~0");
            } else if(esc.charAt(i) == ' ') {
                sb.append("/");
            } else {
                sb.append(esc.charAt(i));
            }
        }
        //return 
        return sb.toString();
    }
}
