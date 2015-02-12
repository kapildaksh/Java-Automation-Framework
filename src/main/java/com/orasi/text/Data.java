/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.text;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import okio.Okio;

/**
 *
 * @author Brian Becker
 */
public class Data {

    public static String get(URL collection) {
        try {
            return Okio.buffer(Okio.source((InputStream)collection.getContent())).readUtf8();
        } catch (IOException ex) {
            throw new RuntimeException("File Not Found", ex);
        }
    }
    
}
