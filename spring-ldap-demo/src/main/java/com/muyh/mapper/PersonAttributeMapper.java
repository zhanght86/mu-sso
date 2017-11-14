package com.muyh.mapper;

import com.muyh.bean.Person;
import org.springframework.ldap.core.AttributesMapper;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

public class PersonAttributeMapper implements AttributesMapper {

    @Override
    public Object mapFromAttributes(Attributes attr) throws NamingException {
        Person person = new Person();
        person.setSn((String) attr.get("sn").get());
        person.setCn((String) attr.get("cn").get());

        if (attr.get("userPassword") != null) {
            person.setUserPassword((String)attr.get("userPassword").get());
        }
        if (attr.get("telephoneNumber") != null) {
            person.setTelephoneNumber((String)attr.get("telephoneNumber").get());
        }
        if (attr.get("seeAlso") != null) {
            person.setSeeAlso((String)attr.get("seeAlso").get());
        }
        if (attr.get("description") != null) {
            person.setDescription((String)attr.get("description").get());
        }
        return person;
    }
}
