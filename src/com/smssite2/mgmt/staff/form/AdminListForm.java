package com.smssite2.mgmt.staff.form;

import com.note.common.page.BasicPagination;

public class AdminListForm extends BasicPagination{
	private String userName;
	private String realName;
	private String roleName;
	private String orgName;
	private String locksign;
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	public String getOrgName() {
		return orgName;
	}
	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	public String getLocksign() {
		return locksign;
	}
	public void setLocksign(String locksign) {
		this.locksign = locksign;
	}
	
	
}
