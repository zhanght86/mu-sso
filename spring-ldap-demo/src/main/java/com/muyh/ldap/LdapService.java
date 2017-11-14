package com.muyh.ldap;

import com.muyh.bean.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.List;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

@Service
public class LdapService {

    private static final String BASE_DN = "dc=my-domain,dc=com";

    @Autowired
    private LdapTemplate ldapTemplate;

    public List<String> getAllPersonNames() {
        return ldapTemplate.search(
                query().where("objectclass").is("person"),
                new AttributesMapper<String>() {
                    @Override
                    public String mapFromAttributes(Attributes attrs)
                            throws NamingException {
                        return (String) attrs.get("cn").get();
                    }
                });
    }

    public Person findPerson(String dn) {
        return ldapTemplate.lookup(dn, new PersonAttributesMapper());
    }

    public List<Person> getAllPersons() {
        return ldapTemplate.search(query()
                .where("objectclass").is("person"), new PersonAttributesMapper());
    }

    public List<String> getPersonNamesByLastName(String lastName) {

        LdapQuery query = query()
                .base("dc=my-domain,dc=com")
                .attributes("cn", "sn")
                .where("objectclass").is("person")
                .and("sn").is(lastName);

        return ldapTemplate.search(query,
                new AttributesMapper<String>() {
                    @Override
                    public String mapFromAttributes(Attributes attrs)
                            throws NamingException {

                        return (String) attrs.get("cn").get();
                    }
                });
    }

    public void create(Person p) {
        Name dn = buildDn(p);
        ldapTemplate.bind(dn, null, buildAttributes(p));
    }

    public void delete(Person p) {
        Name dn = buildDn(p);
        ldapTemplate.unbind(dn);
    }

    public void update(Person p) {
        Name dn = buildDn(p);
        ldapTemplate.rebind(dn, null, buildAttributes(p));
    }

    public void updateDescription(Person p) {
        Name dn = buildDn(p);
        Attribute attr = new BasicAttribute("description", p.getDescription());
        ModificationItem item = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, attr);
        ldapTemplate.modifyAttributes(dn, new ModificationItem[]{item});
    }

//    public Person findByPrimaryKey(String name, String company, String country) {
//        Name dn = buildDn(name, company, country);
//        return (Person) ldapTemplate.lookup(dn, new PersonContextMapper());
//    }

    protected Name buildDn(Person p) {
        return LdapNameBuilder.newInstance(BASE_DN)
//                .add("c", p.getC())
//                .add("ou", p.getOu())
                .add("cn", p.getCn())
                .build();
    }

    private class PersonAttributesMapper implements AttributesMapper<Person> {
        @Override
        public Person mapFromAttributes(Attributes attrs) throws NamingException {
            Person person = new Person();
            person.setCn((String) attrs.get("cn").get());
            person.setSn((String) attrs.get("sn").get());
//            if (attrs.get("userPassword") != null) {
//                person.setUserPassword((String)attrs.get("userPassword").get());
//            }
            if (attrs.get("telephoneNumber") != null) {
                person.setTelephoneNumber((String)attrs.get("telephoneNumber").get());
            }
            if (attrs.get("seeAlso") != null) {
                person.setSeeAlso((String)attrs.get("seeAlso").get());
            }
            if (attrs.get("description") != null) {
                person.setDescription((String)attrs.get("description").get());
            }
            if (attrs.get("c") != null) {
                person.setC((String) attrs.get("c").get());
            }
            if (attrs.get("ou") != null) {
                person.setOu((String) attrs.get("ou").get());
            }


            return person;
        }
    }

    private Attributes buildAttributes(Person p) {
        Attributes attrs = new BasicAttributes();
        BasicAttribute ocattr = new BasicAttribute("objectclass");
        ocattr.add("top");
        ocattr.add("person");
        attrs.put(ocattr);
        attrs.put("cn", "Some Person");
        attrs.put("sn", "Person");
        return attrs;
    }

    private static class PersonContextMapper implements ContextMapper {
        @Override
        public Object mapFromContext(Object ctx) {
            DirContextAdapter context = (DirContextAdapter) ctx;
            Person p = new Person();
            p.setCn(context.getStringAttribute("cn"));
            p.setSn(context.getStringAttribute("sn"));
            p.setDescription(context.getStringAttribute("description"));
            return p;
        }
    }
}
