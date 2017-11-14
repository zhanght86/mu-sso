package com.muyh.bean;

import org.apache.shiro.authc.UsernamePasswordToken;

/**
 * @author chenhaoqiang
 * @Description 登录令牌
 * @Date 2016-1-13
 */
public class UsernamePasswordTokenEx extends UsernamePasswordToken {
	private static final long serialVersionUID = 2944197324170087441L;
	
	private Integer accNum;//账号
	private boolean isRsa = true;
	
	public UsernamePasswordTokenEx(String username, String password, boolean rememberMe, String host) {
		super(username, password, rememberMe, host);
	}

	public Integer getAccNum() {
		return accNum;
	}

	public void setAccNum(Integer accNum) {
		this.accNum = accNum;
	}

	public boolean isRsa() {
		return isRsa;
	}

	public void setRsa(boolean isRsa) {
		this.isRsa = isRsa;
	}
	
}
