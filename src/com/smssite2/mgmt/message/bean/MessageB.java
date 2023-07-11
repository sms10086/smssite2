package com.smssite2.mgmt.message.bean;

import java.sql.Timestamp;

public class MessageB {	private String ID;
	private String staffId;
	private Timestamp scheduleTime;
	private String content;
	private Integer total;
	private Integer splitCount;
	private String smsNum;
	private String smsSign;
	private String phone;
	private String mesType;
	private int flag;
	public String getID() {
		return ID;
	}
	public void setID(String id) {
		ID = id;
	}
	public String getStaffId() {
		return staffId;
	}
	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}
	public Timestamp getScheduleTime() {
		return scheduleTime;
	}
	public void setScheduleTime(Timestamp scheduleTime) {
		this.scheduleTime = scheduleTime;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	public Integer getSplitCount() {
		return splitCount;
	}
	public void setSplitCount(Integer splitCount) {
		this.splitCount = splitCount;
	}
	public String getSmsNum() {
		return smsNum;
	}
	public void setSmsNum(String smsNum) {
		this.smsNum = smsNum;
	}
	public String getSmsSign() {
		return smsSign;
	}
	public void setSmsSign(String smsSign) {
		this.smsSign = smsSign;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getMesType() {
		return mesType;
	}
	public void setMesType(String mesType) {
		this.mesType = mesType;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	 
}
