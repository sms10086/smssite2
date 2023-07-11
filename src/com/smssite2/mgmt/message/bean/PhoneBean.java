package com.smssite2.mgmt.message.bean;

import java.io.Serializable;

public class PhoneBean implements Serializable{
	private static final long serialVersionUID = 1L;
	private String rowid;
	private String phone;
	private String name;
	private String content;
	private Integer phoneType;
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getPhoneType() {
		return phoneType;
	}
	public void setPhoneType(Integer phoneType) {
		this.phoneType = phoneType;
	}
	public String getRowid() {
		return rowid;
	}
	public void setRowid(String rowid) {
		this.rowid = rowid;
	}
	
}
