/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.dataProviders;

import java.util.Iterator;

/**
 * A generic data provider interface, with which we can use to get data as
 * an Iterator of Object[]. This is the format which TestNG expects its data
 * for test cases.
 * 
 * @version     01/06/2015
 * @author      Brian Becker
 */
public interface DataProvider {
    public Iterator<Object[]> getData();
}
