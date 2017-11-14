package com.muyh.util;

import com.muyh.bean.PortalSession;
import com.muyh.util.rsa.PublicKeyUtil;
import com.muyh.util.rsa.RSAUtil;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 
 * @Author: liujian
 * @Description: Session管理器
 * @Date: 2015-10-9
 */
public class SessionManager {

	private SessionManager() {}
	
	public static final String PORTALSESSIONNAME = "portalSession";
	public static final String PORTALPWDKEY = "pwdRandomUUID";

	public static final PortalSession getPortalSession() {
		return (PortalSession) getSession().getAttribute(PORTALSESSIONNAME);
	}
	
	public static final Session getSession() {
		Subject currentUser = SecurityUtils.getSubject();
		Session session = currentUser.getSession();
		return session;
	}
	
	public static final Map<String, Object> setRSAUuid() {
		Session session = getSession();
		if (null == session) {
			return null;
		}
		Map<String, Object> msgMap = new HashMap<String, Object>();
		String uuid = UUID.randomUUID().toString();
		RSAUtil rsaUtil = SpringUtils.getBean("rsaUtil");
		rsaUtil.initKeyFile(uuid);
		PublicKeyUtil publicKeyMap = rsaUtil.getPublicKeyUtil(true);
		session.setAttribute(SessionManager.PORTALPWDKEY, uuid);
		msgMap.put("publicKeyMap", publicKeyMap);
		return msgMap;
	}

	public static final String getRSAUuid(String password) {
		Session session = getSession();
		Object object = session.getAttribute(SessionManager.PORTALPWDKEY);
		if (null == object) {
			return null;
		}
    	RSAUtil rsaUtil = SpringUtils.getBean("rsaUtil");
		rsaUtil.initKeyFile(object.toString());
		return rsaUtil.decryptStringByJs(String.valueOf(password));
	}
}
