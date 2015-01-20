/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.rest;

/**
 *
 * @author brian.becker
 */
public interface RestRequestCollection {
    public abstract RestRequest byName(String name);
}
