package com.muyh.controller;

import com.muyh.bean.Person;
import com.muyh.ldap.LdapDemo;
import com.muyh.ldap.LdapService;
import com.muyh.ldap.PersonRepoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/")
public class LdapCtrl {

    @Autowired
    private LdapDemo ldapDemo;

    @Autowired
    private LdapService ldapService;

    @Autowired
    private PersonRepoService personRepoService;

    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public List<String> test(){
        List<String> ls = ldapDemo.getAllPersonNames();
        return ls;
    }

    @RequestMapping(value = "/addPerson",method = RequestMethod.POST)
    public String addPerson(Person person){
        ldapDemo.createOnePerson(person);
        return "123";
    }

    @RequestMapping(value = "/getPersonList",method = RequestMethod.POST)
    public String getPersonList(Person person){
        ldapDemo.createOnePerson(person);
        return "123";
    }


    @RequestMapping(value = "/getAllPersons",method = RequestMethod.GET)
    public List<Person> getAllPersons(){
        return personRepoService.findAll();
//        return ldapService.getAllPersons();
    }

    @RequestMapping(value = "/findByPrimaryKey",method = RequestMethod.POST)
    public Person findByPrimaryKey(String name, String company){
        return personRepoService.findByPrimaryKey(name,company,"");
    }

    @RequestMapping(value = "/findByName",method = RequestMethod.POST)
    public List<String> findByName(String cn){
        return personRepoService.findByName(cn);
    }

    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public String create(Person person){
        personRepoService.create(person);
        return "success";
    }

    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public String delete(Person person){
        personRepoService.delete(person);
        return "success";
    }

    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public String update(Person person){
        personRepoService.update(person);
        return "success";
    }

//    @RequestMapping(value = "/updateDescription",method = RequestMethod.POST)
//    public String updateDescription(Person person){
//        ldapService.updateDescription(person);
//        return "success";
//    }

}
