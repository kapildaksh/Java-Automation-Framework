/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.rest;

import java.util.Map;

/**
 * The REST Warehouse provides a loader for REST API specifications in a
 * given format.
 * 
 * @author Brian Becker
 */
public interface RestWarehouse {
    public abstract RestCollection collection(String name);
    public Map environment(String name);
}
