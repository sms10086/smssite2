package com.note.bean;

import java.sql.Timestamp;

public class Phone {
	private int ID;
	private String phone;
	private String name;
	private int phoneType;
	private String content;
	private String sessionID;
	private String eid;
	private String staffID;
	private Timestamp bornDate ;
	private String taskID;
	public int getID() {
		return ID;
	}
	public void setID(int id) {
		ID = id;
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
	public int getPhoneType() {
		return phoneType;
	}
	public void setPhoneType(int phoneType) {
		this.phoneType = phoneType;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSessionID() {
		return sessionID;
	}
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	public String getEid() {
		return eid;
	}
	public void setEid(String eid) {
		this.eid = eid;
	}
	public String getStaffID() {
		return staffID;
	}
	public void setStaffID(String staffID) {
		this.staffID = staffID;
	}
	public Timestamp getBornDate() {
		return bornDate;
	}
	public void setBornDate(Timestamp bornDate) {
		this.bornDate = bornDate;
	}
	public String getTaskID() {
		return taskID;
	}
	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}
}
