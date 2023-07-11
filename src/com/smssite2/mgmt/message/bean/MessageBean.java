package com.smssite2.mgmt.message.bean;

import java.sql.Timestamp;

public class MessageBean {
	private String messageId;
	private String staffId;
	private String needSendTime;
	private String content;
	private Integer total;
	private Integer sentCount;
	private String smsNum;
	private String smsSign;
	private Integer isSchedule;
	private Timestamp scheduleTime;
	
	private String phone;
	private String name;
	private String pcontent;
	public Timestamp getScheduleTime() {
		return scheduleTime;
	}
	public void setScheduleTime(Timestamp scheduleTime) {
		this.scheduleTime = scheduleTime;
	}
	public String getStaffId() {
		return staffId;
	}
	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}
	
	
	public String getNeedSendTime() {
		return needSendTime;
	}
	public void setNeedSendTime(String needSendTime) {
		this.needSendTime = needSendTime;
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
	
	public Integer getSentCount() {
		return sentCount;
	}
	public void setSentCount(Integer sentCount) {
		this.sentCount = sentCount;
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
	public Integer getIsSchedule() {
		return isSchedule;
	}
	public void setIsSchedule(Integer isSchedule) {
		this.isSchedule = isSchedule;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
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
	public String getPcontent() {
		return pcontent;
	}
	public void setPcontent(String pcontent) {
		this.pcontent = pcontent;
	}
}
