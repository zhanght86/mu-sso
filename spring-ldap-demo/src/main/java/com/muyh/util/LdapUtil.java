package com.muyh.util;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class LdapUtil {

    private static String ROOT = "dc=my-domain,dc=com";
    private static String LDAP_URL = "ldap://192.168.1.212:389"; // LDAP访问地址
    private static String USERNAME = "cn=Manager,dc=my-domain,dc=com"; // 用户名
    private static String PASSWORD = "123456"; // 密码

    private String dn = "";
    private DirContext dc;

    private String employeeId = "";

    public LdapUtil() {
        init();
//        close();
    }

    public void init() {
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY,
                "com.sun.jndi.ldap.LdapCtxFactory");
        env.put(Context.PROVIDER_URL, LDAP_URL);
        env.put(Context.SECURITY_AUTHENTICATION, "simple");
        env.put(Context.SECURITY_PRINCIPAL, USERNAME);
        env.put(Context.SECURITY_CREDENTIALS, PASSWORD);
        try {
            dc = new InitialDirContext(env);// 初始化上下文
            System.out.println("认证成功");// 这里可以改成异常抛出。
        } catch (javax.naming.AuthenticationException e) {
            System.out.println("认证失败");
        } catch (Exception e) {
            System.out.println("认证出错：" + e);
        }
    }

    /**
     * 添加
     */
    public void add(String newUserName) {
        try {
            BasicAttributes attrs = new BasicAttributes();
            BasicAttribute objclassSet = new BasicAttribute("objectClass");
            objclassSet.add("sAMAccountName");
            objclassSet.add("employeeID");
            attrs.put(objclassSet);
            attrs.put("ou", newUserName);
            dc.createSubcontext("ou=" + newUserName + "," + ROOT, attrs);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception in add():" + e);
        }
    }

    /**
     * 删除
     *
     * @param dn
     */
    public void delete(String dn) {
        try {
            dc.destroySubcontext(dn);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception in delete():" + e);
        }
    }

    /**
     * 重命名节点
     *
     * @param oldDN
     * @param newDN
     * @return
     */
    public boolean renameEntry(String oldDN, String newDN) {
        try {
            dc.rename(oldDN, newDN);
            return true;
        } catch (NamingException ne) {
            System.err.println("Error: " + ne.getMessage());
            return false;
        }
    }


    /**
     * 修改
     *
     * @return
     */
    public boolean modifyInformation(String dn, String employeeID) {
        try {
            System.out.println("updating...\n");
            ModificationItem[] mods = new ModificationItem[1];
                         /* 修改属性 */
            // Attribute attr0 = new BasicAttribute("employeeID", "W20110972");
            // mods[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
            // attr0);
                         /* 删除属性 */
            // Attribute attr0 = new BasicAttribute("description",
            // "陈轶");
            // mods[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE,
            // attr0);
                         /* 添加属性 */
            Attribute attr0 = new BasicAttribute("host", employeeID);
            mods[0] = new ModificationItem(DirContext.ADD_ATTRIBUTE, attr0);
                         /* 修改属性 */
            dc.modifyAttributes(dn , mods);
            System.out.println("修改成功");
            return true;
        } catch (NamingException e) {
            e.printStackTrace();
            System.err.println("Error: " + e.getMessage());
            return false;
        }
    }

    /**
     * @param base   ：根节点(在这里是"dc=example,dc=com")
     * @param scope  ：搜索范围,分为"base"(本节点),"one"(单层),""(遍历)
     * @param filter ：指定子节点(格式为"(objectclass=*)",*是指全部，你也可以指定某一特定类型的树节点)
     */
    public void searchInformation(String base, String scope, String filter) {
        SearchControls sc = new SearchControls();
        if (scope.equals("base")) {
            sc.setSearchScope(SearchControls.OBJECT_SCOPE);
        } else if (scope.equals("one")) {
            sc.setSearchScope(SearchControls.ONELEVEL_SCOPE);
        } else {
            sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
        }
        NamingEnumeration ne = null;
        try {
            ne = dc.search(base, filter, sc);
            // Use the NamingEnumeration object to cycle through
            // the result set.
            while (ne.hasMore()) {
                System.out.println();
                SearchResult sr = (SearchResult) ne.next();
                String name = sr.getName();
                if (base != null && !base.equals("")) {
                    System.out.println("entry: " + name + "," + base);
                } else {
                    System.out.println("entry: " + name);
                }

                Attributes at = sr.getAttributes();
                NamingEnumeration ane = at.getAll();
                while (ane.hasMore()) {
                    Attribute attr = (Attribute) ane.next();
                    String attrType = attr.getID();
                    NamingEnumeration values = attr.getAll();
//                    Vector vals = new Vector();
                    // Another NamingEnumeration object, this time
                    // to iterate through attribute values.
                    while (values.hasMore()) {
                        Object oneVal = values.nextElement();
                        if (oneVal instanceof String) {
                            System.out.println(attrType + ": "
                                    + (String) oneVal);
                        } else {
                            System.out.println(attrType + ": "
                                    + new String((byte[]) oneVal));
                        }
                    }
                }
            }
        } catch (Exception nex) {
            System.err.println("Error: " + nex.getMessage());
            nex.printStackTrace();
        }
    }

    /**
     * 查询
     *
     * @throws NamingException
     */
    public void Ldapbyuserinfo(String userName) {
        // Create the search controls
        SearchControls searchCtls = new SearchControls();
        // Specify the search scope
        searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
        // specify the LDAP search filter
//        String searchFilter = "sAMAccountName=" + userName;
        String searchFilter = "cn=" + userName;
        // Specify the Base for the search 搜索域节点
        String searchBase = "DC=my-domain,DC=COM";
        int totalResults = 0;
        String returnedAtts[] = {"url", "whenChanged", "employeeID", "name",
                "userPrincipalName", "physicalDeliveryOfficeName",
                "departmentNumber", "telephoneNumber", "homePhone", "mobile",
                "department", "sAMAccountName", "whenChanged", "mail"}; // 定制返回属性

//        searchCtls.setReturningAttributes(returnedAtts); // 设置返回属性集

         searchCtls.setReturningAttributes(null); // 不定制属性，将返回所有的属性集

        try {
            NamingEnumeration answer = dc.search(searchBase, searchFilter,
                    searchCtls);
            if (answer == null || answer.equals(null)) {
                System.out.println("answer is null");
            } else {
                System.out.println("answer not null");
            }
            while (answer.hasMoreElements()) {
                SearchResult sr = (SearchResult) answer.next();
                System.out
                        .println("************************************************");
                System.out.println("getname=" + sr.getName());
                Attributes Attrs = sr.getAttributes();
                if (Attrs != null) {
                    try {

                        for (NamingEnumeration ne = Attrs.getAll(); ne
                                .hasMore(); ) {
                            Attribute Attr = (Attribute) ne.next();
                            System.out.println("AttributeID="
                                    + Attr.getID().toString());
                            // 读取属性值
                            for (NamingEnumeration e = Attr.getAll(); e.hasMore(); totalResults++) {
                                String user = e.next().toString(); // 接受循环遍历读取的userPrincipalName用户属性
                                System.out.println(user);
                            }
                            // System.out.println(" ---------------");
                            // // 读取属性值
                            // Enumeration values = Attr.getAll();
                            // if (values != null) { // 迭代
                            // while (values.hasMoreElements()) {
                            // System.out.println(" 2AttributeValues="
                            // + values.nextElement());
                            // }
                            // }
                            // System.out.println(" ---------------");
                        }
                    } catch (NamingException e) {
                        System.err.println("Throw Exception : " + e);
                    }
                }
            }
            System.out.println("Number: " + totalResults);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Throw Exception : " + e);
        }
    }


    public void close() {
        if (dc != null) {
            try {
                dc.close();
            } catch (NamingException e) {
                System.out.println("NamingException in close():" + e);
            }
        }
    }

    public static void main(String[] args) {
        LdapUtil ldapUtil = new LdapUtil();
//        ldapUtil.searchInformation("ou=People,dc=my-domain,dc=com","base","(objectclass=*)");
//        ldapUtil.Ldapbyuserinfo("ldapuser1");

        ldapUtil.modifyInformation("uid=ldapuser1,ou=People,dc=my-domain,dc=com","123123");
        ldapUtil.close();
    }
}