package com.muyh.ldap;

import com.muyh.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.*;
import org.springframework.ldap.core.support.AbstractContextMapper;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.ldap.support.LdapUtils;
import org.springframework.stereotype.Service;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class PersonRepoService {
    @Autowired
    private LdapTemplate ldapTemplate;

    public void setLdapTemplate(LdapTemplate ldapTemplate) {
        this.ldapTemplate = ldapTemplate;
    }

    public void create(Person person) {
        DirContextAdapter context = new DirContextAdapter(buildDn(person));
        mapToContext(person, context);
        ldapTemplate.bind(context);
    }

    public void update(Person person) {
        Name dn = buildDn(person);
        DirContextOperations context = ldapTemplate.lookupContext(dn);
        mapToContext(person, context);
        ldapTemplate.modifyAttributes(context);
    }

    public void delete(Person person) {
        ldapTemplate.unbind(buildDn(person));
    }

    public Person findByPrimaryKey(String name, String company, String country) {
        Name dn = buildDn(name, company, country);
        return (Person) ldapTemplate.lookup(dn, getContextMapper());
    }

    public List findByName(String name) {
        LdapQuery query = query()
                .where("objectclass").is("person")
                .and("cn").whitespaceWildcardsLike("name");

        return ldapTemplate.search(query, getContextMapper());
    }

    public List findAll() {
        EqualsFilter filter = new EqualsFilter("objectclass", "person");
        return ldapTemplate.search(LdapUtils.emptyLdapName(), filter.encode(), getContextMapper());
    }

    protected ContextMapper getContextMapper() {
        return (ContextMapper) new PersonContextMapper();
    }

    protected Name buildDn(Person person) {
        return buildDn(person.getCn(), person.getOu(), person.getC());
    }

    protected Name buildDn(String fullname, String company, String country) {
        return LdapNameBuilder.newInstance()
//                .add("c", country)
                .add("ou", company)
                .add("cn", fullname)
                .build();
    }

    protected void mapToContext(Person person, DirContextOperations context) {
        context.setAttributeValues("objectclass", new String[] {"top", "person"});
        context.setAttributeValue("cn", person.getCn());
        context.setAttributeValue("sn", person.getSn());
        context.setAttributeValue("description", person.getDescription());
    }

    private static class PersonContextMapper extends AbstractContextMapper<Person> {
        @Override
        public Person doMapFromContext(DirContextOperations context) {
            Person person = new Person();
            person.setCn(context.getStringAttribute("cn"));
            person.setSn(context.getStringAttribute("sn"));
            person.setDescription(context.getStringAttribute("description"));
            return person;
        }
    }
}
