package com.smssite2.mgmt.staff.form;

import org.apache.struts.action.ActionForm;

public class LoginForm extends ActionForm {	private String EId;
	private String staffId;
	private String password;
	private String checkCode;

	public LoginForm() {
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}


	public String getEId() {
		return EId;
	}

	public void setEId(String id) {
		EId = id;
	}


	public String getStaffId() {
		return staffId;
	}

	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


}
