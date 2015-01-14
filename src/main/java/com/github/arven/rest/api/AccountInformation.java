/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.arven.rest.api;

/**
 *
 * @author brian.becker
 */
public class AccountInformation {   
    public String username;
    public String nickname;
    
    public AccountInformation() { }
    
    public AccountInformation(String username, String nickname) {
        this.username = username;
        this.nickname = nickname;
    }
    
}
