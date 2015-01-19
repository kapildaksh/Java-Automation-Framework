/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.arven.sandbox;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author brian.becker
 */
public class Post {
    public Integer id;
    public Date created;
    public String text;
       
    public List<String> getTags() throws Exception {
        List<String> tags = new ArrayList<String>();
        String[] temp = StringUtils.substringsBetween(text.replaceAll("[" + Pattern.quote("!\"$%&'()*+,-./:;<=>?@[\\]^_`{|}~") + "]", " ").concat(" "), "#", " ");
        if (temp != null) {
            for(String tag : temp) {
                tags.add(StringUtils.lowerCase(tag));
            }
        }
        return tags;
    }
}
