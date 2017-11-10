package com.muyh.controller;

import com.muyh.ldap.LdapDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/")
public class LdapCtrl {

    @Autowired
    private LdapDemo ldapDemo;

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public List<String> test(){
        List<String> ls = ldapDemo.getAllPersonNames();
        return ls;
    }

}
