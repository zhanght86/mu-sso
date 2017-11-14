package com.muyh.bean;

import java.io.Serializable;
import java.util.Calendar;

/**
 * 
 * @Author: liuj
 * @Description: 持卡人session
 * @Date: 2015-10-22
 */
public class PortalSession implements Serializable {

	private static final long serialVersionUID = -7415472745023023695L;

	private Integer accNum;// 帐号
	private String accName; // 持卡人名称
	private Integer epId; // 使用单位编号
	private Calendar loginTime; // 登录时间
	private String loginTimeStr; // 登录时间显示
	private String accDepName; // 账户部门名称
	private String perCode;// 人员编号
	private String periodTime;// 时间段
	private String idNo;// 身份证号
	private Integer accClassId;// 身份编号
	private String disableDate;// 失效日期
	private Integer isDefaultPWD;// 是否默认服务密码
	private Integer epTypeFlag;// 使用单位标志
	private Integer cardStatus;// 卡现状态（兰大班特有）
	private String maintPhone;// 维护电话（暂时只有标准版有）
	private String maintDep;// 维护部门（暂时只有标准版有）
	private Integer accCardCount;// 账户持卡数量
	private String natureIdNo;//证件号
	private String allowportalrefund;//销户结算的
	private String selfeditStr;//账户身份的自助修改信息字段
	private Integer acctype;//人员类型

	public Integer getEpId() {
		return epId;
	}

	public void setEpId(Integer epId) {
		this.epId = epId;
	}

	public Calendar getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Calendar loginTime) {
		this.loginTime = loginTime;
		setPeriodTime(loginTime);
	}

	public String getLoginTimeStr() {
		return loginTimeStr;
	}

	public void setLoginTimeStr(String loginTimeStr) {
		this.loginTimeStr = loginTimeStr;
	}

	public String getAccName() {
		return accName;
	}

	public void setAccName(String accName) {
		this.accName = accName;
	}

	public Integer getAccNum() {
		return accNum;
	}

	public void setAccNum(Integer accNum) {
		this.accNum = accNum;
	}

	public String getAccDepName() {
		return accDepName;
	}

	public void setAccDepName(String accDepName) {
		this.accDepName = accDepName;
	}

	public String getPerCode() {
		return perCode;
	}

	public void setPerCode(String perCode) {
		this.perCode = perCode;
	}

	public String getPeriodTime() {
		return periodTime;
	}

	private void setPeriodTime(Calendar loginTime) {
		int h = loginTime.get(Calendar.HOUR_OF_DAY);
		if (h >= 0 && h < 11) {
			this.periodTime = "index.page.021";
		} else if (h >= 11 && h < 13) {
			this.periodTime = "index.page.022";
		} else if (h >= 13 && h < 18) {
			this.periodTime = "index.page.023";
		} else if (h >= 18 && h < 24) {
			this.periodTime = "index.page.024";
		}
	}

	public String getIdNo() {
		return idNo;
	}

	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}

	public Integer getAccClassId() {
		return accClassId;
	}

	public void setAccClassId(Integer accClassId) {
		this.accClassId = accClassId;
	}

	public String getDisableDate() {
		return disableDate;
	}

	public void setDisableDate(String disableDate) {
		this.disableDate = disableDate;
	}

	public Integer getIsDefaultPWD() {
		return isDefaultPWD;
	}

	public void setIsDefaultPWD(Integer isDefaultPWD) {
		this.isDefaultPWD = isDefaultPWD;
	}

	public Integer getEpTypeFlag() {
		return epTypeFlag;
	}

	public void setEpTypeFlag(Integer epTypeFlag) {
		this.epTypeFlag = epTypeFlag;
	}

	public Integer getCardStatus() {
		return cardStatus;
	}

	public void setCardStatus(Integer cardStatus) {
		this.cardStatus = cardStatus;
	}

	public String getMaintPhone() {
		return maintPhone;
	}

	public void setMaintPhone(String maintPhone) {
		this.maintPhone = maintPhone;
	}

	public String getMaintDep() {
		return maintDep;
	}

	public void setMaintDep(String maintDep) {
		this.maintDep = maintDep;
	}

	public Integer getAccCardCount() {
    	return accCardCount;
    }

	public void setAccCardCount(Integer accCardCount) {
    	this.accCardCount = accCardCount;
    }

	public String getNatureIdNo() {
		return natureIdNo;
	}

	public void setNatureIdNo(String natureIdNo) {
		this.natureIdNo = natureIdNo;
	}

	public String getAllowportalrefund() {
		return allowportalrefund;
	}

	public void setAllowportalrefund(String allowportalrefund) {
		this.allowportalrefund = allowportalrefund;
	}

	public String getSelfeditStr() {
		return selfeditStr;
	}

	public void setSelfeditStr(String selfeditStr) {
		this.selfeditStr = selfeditStr;
	}

	public Integer getAcctype() {
		return acctype;
	}

	public void setAcctype(Integer acctype) {
		this.acctype = acctype;
	}
	
}
