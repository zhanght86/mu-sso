package com.muyh.util;

import com.hzsun.easytong.commons.Constants;
import com.hzsun.easytong.commons.utils.DES3Coder;
import com.muyh.bean.UsernamePasswordTokenEx;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;

public class MySimpleCredentialsMatcher extends SimpleCredentialsMatcher {

	@Override
	public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
		UsernamePasswordTokenEx tokeneEx = (UsernamePasswordTokenEx) token;
		Object tokenCredentials = null;
		tokenCredentials = new sun.misc.BASE64Encoder().encode(DES3Coder.encryptionByDES3(Integer.valueOf(tokeneEx.getAccNum()),
				String.valueOf(tokeneEx.getPassword()).getBytes(), 0));
		Object accountCredentials = getCredentials(info);
		// 将密码加密与系统加密后的密码校验，内容一致就返回true,不一致就返回false
		return equals(tokenCredentials, accountCredentials);
	}

}
