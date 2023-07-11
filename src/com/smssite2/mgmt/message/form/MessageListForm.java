package com.smssite2.mgmt.message.form;

import com.note.common.page.BasicPagination;

public class MessageListForm extends BasicPagination{
	private String userName;
	private String content;
	private String phone;
	private String mesType;
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getMesType() {
		return mesType;
	}
	public void setMesType(String mesType) {
		this.mesType = mesType;
	}
	
}
