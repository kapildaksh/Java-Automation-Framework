/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.dataProviders;

import java.util.Iterator;

/**
 *
 * @author Brian Becker
 */
public interface DataProvider {
    public Iterator<Object[]> getData();
}
