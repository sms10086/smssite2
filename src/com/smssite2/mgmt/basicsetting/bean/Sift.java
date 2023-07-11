package com.smssite2.mgmt.basicsetting.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class Sift implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String siftId;
	private String siftContent;
	private String staffId;
	private String EID;
	private Timestamp addTime;
	
	
	public String getSiftContent() {
		return siftContent;
	}
	public void setSiftContent(String siftContent) {
		this.siftContent = siftContent;
	}
	public String getSiftId() {
		return siftId;
	}
	public void setSiftId(String siftId) {
		this.siftId = siftId;
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
	public Timestamp getAddTime() {
		return addTime;
	}
	public void setAddTime(Timestamp addTime) {
		this.addTime = addTime;
	}
	
}
