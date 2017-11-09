package com.muyh.ldap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.odm.annotations.Attribute;

import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

public class LdapDemo {

    @Autowired
    private LdapTemplate ldapTemplate;

//    public List<String> getAllPersonNames(){
//        return ldapTemplate.search({
//                query().where("objectclass").is("person"),
//                new AttributesMapper<String>(){
//                    public String mapFromAttributes(Attribute attrs)throws NamingException {
//                        return (String) attrs.get("cn").get();
//
//                    }
//                }
//        });
//    }

}
