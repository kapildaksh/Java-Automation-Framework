/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.rest;

import java.util.Map;

/**
 *
 * @author brian.becker
 */
public interface RestCollection {
    public abstract RestRequest byId(String id);
    public abstract RestRequest byName(String name);
    public abstract RestCollection env(Map variables);
}