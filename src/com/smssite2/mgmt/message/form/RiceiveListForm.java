package com.smssite2.mgmt.message.form;

import com.note.common.page.BasicPagination;

public class RiceiveListForm extends BasicPagination{
	private String start;
	private String end;
	private String receicer;
	private String content;
	private String phone;
	private String sender;
	
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getReceicer() {
		return receicer;
	}
	public void setReceicer(String receicer) {
		this.receicer = receicer;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
