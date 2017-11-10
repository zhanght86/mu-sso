package com.muyh.ldap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.NamingException;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


import javax.naming.Name;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import javax.naming.ldap.LdapName;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class LdapDemo {

    @Autowired
    private LdapTemplate ldapTemplate;

    public List<String> getAllPersonNames() {

        List<String> ls = ldapTemplate.search(
                query().where("objectclass").is("person"),
                new AttributesMapper<String>() {
                    @Override
                    public String mapFromAttributes(Attributes attrs)
                            throws NamingException {
                        try {
                            return attrs.get("cn").get().toString();
                        } catch (javax.naming.NamingException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                });
        System.out.println(ls);
        return ls;
    }

    private DistinguishedName buildDn() {
        DistinguishedName dn = new DistinguishedName();
        dn.append("ou", "People");
        return dn;
    }

}
