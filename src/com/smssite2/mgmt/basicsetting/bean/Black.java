package com.smssite2.mgmt.basicsetting.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class Black implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String blackId;
	private String blackPhoneNum;
	private String staffId;
	private String EID;
	private String addTime;
	
	
	public String getBlackId() {
		return blackId;
	}
	public void setBlackId(String blackId) {
		this.blackId = blackId;
	}
	public String getBlackPhoneNum() {
		return blackPhoneNum;
	}
	public void setBlackPhoneNum(String blackPhoneNum) {
		this.blackPhoneNum = blackPhoneNum;
	}
	
	public String getStaffId() {
		return staffId;
	}
	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getEID() {
		return EID;
	}
	public void setEID(String eid) {
		EID = eid;
	}
	public String getAddTime() {
		return addTime;
	}
	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}
	
	
}
