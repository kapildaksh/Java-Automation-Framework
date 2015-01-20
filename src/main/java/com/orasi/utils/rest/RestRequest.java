/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.orasi.utils.rest;

import com.squareup.okhttp.Response;

/**
 *
 * @author Brian Becker
 */
public interface RestRequest {
    public abstract Response send(String... contents) throws Exception;
}
